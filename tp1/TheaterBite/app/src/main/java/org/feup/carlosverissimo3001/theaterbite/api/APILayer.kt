package org.feup.carlosverissimo3001.theaterbite.api

import android.content.Context
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import org.feup.carlosverissimo3001.theaterbite.Constants
import org.feup.carlosverissimo3001.theaterbite.models.ConfirmedOrder
import org.feup.carlosverissimo3001.theaterbite.models.Order
import org.feup.carlosverissimo3001.theaterbite.models.Product
import org.feup.carlosverissimo3001.theaterbite.models.Voucher
import org.feup.carlosverissimo3001.theaterbite.models.parseVoucher
import org.json.JSONArray
import org.json.JSONObject

// Handles the API calls
class APILayer (private val ctx: Context){
    private val client = OkHttpClient()

    fun getPublicKey(user_id: String, callback: (String) -> Unit) {
        val request = okhttp3.Request.Builder()
            .url("${Constants.URL}/get_user_pkey?user_id=$user_id")
            .get()
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                e.printStackTrace()
                // Pass an empty string to the callback to indicate failure
                callback("")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val responseCode = response.code

                // 200 means the user was found
                if (responseCode == 200) {
                    val responseBody = response.body?.string()
                    val jsonResponse = responseBody?.let { JSONObject(it) }
                    val publicKey = jsonResponse?.getString("publickey")


                    // Pass the public key to the callback
                    if (publicKey != null) {
                        callback(publicKey)
                    }

                } else if (responseCode == 404) {
                    // user_id not found
                    println("User not found")
                    // Pass an empty string to the callback to indicate failure
                    callback("")
                }
            }
        })
    }

    fun submitOrder(
        user_id: String,
        order: Order,
        onSuccess: (ConfirmedOrder) -> Unit,
        onError: (Int) -> Unit
    ){
        val client = OkHttpClient()

        val jsonOrder = JSONObject()
        jsonOrder.put("vouchers_used", JSONArray(order.vouchersUsed))
        val barOrder = JSONObject()
        val items = JSONArray()

        for (item in order.products) {
            var itemJson = JSONObject()
            itemJson.put("itemname", item.name)
            itemJson.put("quantity", item.quantity)
            itemJson.put("price", item.price)
            items.put(itemJson)
        }

        barOrder.put("items", items)
        barOrder.put("total", order.total)

        jsonOrder.put("order", barOrder)
        jsonOrder.put("user_id", user_id)

        val requestBody = jsonOrder.toString()
            .toRequestBody("application/json".toMediaTypeOrNull())

//    val requestBody = encrypt(jsonOrder.toString())
//        .toRequestBody("application/json".toMediaTypeOrNull())

        val request = okhttp3.Request.Builder()
            .url("${Constants.URL}/submit_order")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                e.printStackTrace()
                onError(0)
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                when (response.code) {
                    200, 201 -> {
                        val responseBody = response.body?.string()
                        val jsonResponse = responseBody?.let { JSONObject(it) }

                        val confirmedOrder = parseJSONResponse(jsonResponse!!)
                        onSuccess(confirmedOrder)
                    }
                    else -> {
                        onError(response.code)
                    }
                }
            }
        })
    }
}

fun parseJSONResponse(jsonResponse: JSONObject): ConfirmedOrder {
    var orderObject = jsonResponse.getJSONObject("order")

    val orderNumber = orderObject.getInt("order_number")
    val total = orderObject.getDouble("total")
    val items = orderObject.getJSONArray("items")
    val vouchersUsed = orderObject.getJSONArray("vouchers_used")
    val vouchersGenerated = orderObject.getJSONArray("vouchers_generated")

    val products = mutableListOf<Product>()
    for (i in 0 until items.length()) {
        val item = items.getJSONObject(i)
        val name = item.getString("itemname")
        val quantity = item.getInt("quantity")
        val price = item.getDouble("price")
        products.add(Product(name, price, quantity))
    }

    val vouchersUsedList = mutableListOf<Voucher>()
    for (i in 0 until vouchersUsed.length()) {
        vouchersUsedList.add(parseVoucher(vouchersUsed.getJSONObject(i)))
    }

    val vouchersGeneratedList = mutableListOf<Voucher>()
    for (i in 0 until vouchersGenerated.length()) {
        vouchersGeneratedList.add(parseVoucher(vouchersGenerated.getJSONObject(i)))
    }

    return ConfirmedOrder(orderNumber, products, total, vouchersUsedList, vouchersGeneratedList)
}
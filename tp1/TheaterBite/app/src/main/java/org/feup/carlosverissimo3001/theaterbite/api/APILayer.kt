package org.feup.carlosverissimo3001.theaterbite.api

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import org.feup.carlosverissimo3001.theaterbite.Constants
import org.feup.carlosverissimo3001.theaterbite.models.ConfirmedOrder
import org.feup.carlosverissimo3001.theaterbite.models.Order
import org.feup.carlosverissimo3001.theaterbite.parseJSONResponse
import org.json.JSONArray
import org.json.JSONObject

/***** APILayer.kt *****/
// Description: Deals with the communication with the API
/***** APILayer.kt *****/

/**
 * Retrieve the public key of a user from the API
 * @param userId user_id of the user
 * @param callback callback function to pass the public key to
 */
fun getPublicKey(userId: String, callback: (String) -> Unit) {
    val client = OkHttpClient()

    val request = okhttp3.Request.Builder()
        .url("${Constants.URL}/get_user_pkey?user_id=$userId")
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

/**
 * Submit an order to the API
 * @param userId user_id of the user
 * @param order order to submit
 * @param onSuccess callback function to pass the confirmed order to
 * @param onError callback function to call in case of error
 */
fun submitOrder(userId: String, order: Order, onSuccess: (ConfirmedOrder) -> Unit, onError: () -> Unit){
    val client = OkHttpClient()

    val jsonOrder = JSONObject()
    jsonOrder.put("vouchers_used", JSONArray(order.vouchersUsed))
    val barOrder = JSONObject()
    val items = JSONArray()

    for (item in order.products) {
        val itemJson = JSONObject()
        itemJson.put("itemname", item.name)
        itemJson.put("quantity", item.quantity)
        itemJson.put("price", item.price)
        items.put(itemJson)
    }

    barOrder.put("items", items)
    barOrder.put("total", order.total)

    jsonOrder.put("order", barOrder)
    jsonOrder.put("user_id", userId)

    val requestBody = jsonOrder.toString()
        .toRequestBody("application/json".toMediaTypeOrNull())

    val request = okhttp3.Request.Builder()
        .url("${Constants.URL}/submit_order")
        .post(requestBody)
        .build()

    client.newCall(request).enqueue(object : okhttp3.Callback {
        override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
            e.printStackTrace()
            onError()
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
                    onError()
                }
            }
        }
    })
}


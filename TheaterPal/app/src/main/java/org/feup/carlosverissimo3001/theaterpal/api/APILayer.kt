package org.feup.carlosverissimo3001.theaterpal.api

import android.content.Context
import android.util.Base64
import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import org.feup.carlosverissimo3001.theaterpal.Constants
import org.feup.carlosverissimo3001.theaterpal.auth.Authentication
import org.feup.carlosverissimo3001.theaterpal.file.appendTicketsToCache
import org.feup.carlosverissimo3001.theaterpal.file.appendVouchersToCache
import org.feup.carlosverissimo3001.theaterpal.file.areImagesStoreInCache
import org.feup.carlosverissimo3001.theaterpal.file.saveImageToCache
import org.feup.carlosverissimo3001.theaterpal.models.*
import org.feup.carlosverissimo3001.theaterpal.models.Parser.userToJson
import org.feup.carlosverissimo3001.theaterpal.models.Parser.parseOrderRcv
import org.feup.carlosverissimo3001.theaterpal.models.Parser.parseShow
import org.feup.carlosverissimo3001.theaterpal.models.Parser.parseTicket
import org.feup.carlosverissimo3001.theaterpal.models.Parser.parseVoucher
import org.feup.carlosverissimo3001.theaterpal.models.order.*
import org.feup.carlosverissimo3001.theaterpal.models.show.*
import org.feup.carlosverissimo3001.theaterpal.models.transaction.*
import org.json.JSONObject

/**** POST REQUESTS ****/

/**
 * Function to register a user
 * @param ctx context of the application
 * @param user user object to register
 * @param callback callback function to handle the response
 * @see User
 */
fun registerUser(ctx: Context, user: User, callback: (Boolean, String) -> Unit){
    val client = OkHttpClient()

    val jsonObject = JSONObject(userToJson(user))

    // Sign the message with the private key
    val jsonStr = jsonObject.toString()
    val signature = Authentication(ctx).sign(jsonStr)

    // Create JSON object containing data and signature
    val signedJson = JSONObject()
    signedJson.put("data", jsonStr)
    signedJson.put("signature", Base64.encodeToString(signature, Base64.DEFAULT))

    val requestBody = signedJson.toString()
        .toRequestBody("application/json".toMediaTypeOrNull())

    val request = okhttp3.Request.Builder()
        .url("${Constants.URL}/register")
        .post(requestBody)
        .build()

    client.newCall(request).enqueue(object : okhttp3.Callback {
        override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
            e.printStackTrace()
            callback(false, "")
        }

        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
            when (response.code) {
                200,201 -> {
                    val responseBody = response.body?.string()
                    val jsonResponse = responseBody?.let { JSONObject(it) }
                    val userid = jsonResponse?.getString("user_id") ?: ""

                    callback(true, userid)
                }
                else -> {
                    callback(false, "")
                }
            }
        }
    })
}

/**
 * Function to make a ticket purchase
 * @param ctx context of the application
 * @param showDateId id of the show date
 * @param numTickets number of tickets to purchase
 * @param totalCost total cost of the tickets
 */
fun purchaseTickets(ctx: Context, showDateId: Int, numTickets: Int, totalCost: Int, callback: (Boolean) -> Unit){
    val client = OkHttpClient()

    val jsonOrder = JSONObject()
    jsonOrder.put("show_date_id", showDateId)
    jsonOrder.put("num_tickets", numTickets)
    jsonOrder.put("total_cost", totalCost)
    jsonOrder.put("user_id", Authentication(ctx).getUserID())

    val jsonStr = jsonOrder.toString()

    // Signature bytes
    val signature = Authentication(ctx).sign(jsonStr)

    // Create JSON object containing data and signature
    val signedJson = JSONObject()
    signedJson.put("data", jsonStr)
    signedJson.put("signature", Base64.encodeToString(signature, Base64.DEFAULT))

    val requestBody = signedJson.toString()
        .toRequestBody("application/json".toMediaTypeOrNull())

    val request = okhttp3.Request.Builder()
        .url("${Constants.URL}/purchase_tickets")
        .post(requestBody)
        .build()

    client.newCall(request).enqueue(object : okhttp3.Callback {
        override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
            e.printStackTrace()
        }

        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
            when (response.code) {
                200, 201 -> {
                    print("Tickets purchased")

                    val responseBody = response.body?.string()
                    val jsonResponse = responseBody?.let { JSONObject(it) }
                    val tickets = jsonResponse?.getJSONArray("tickets")

                    if (tickets != null) {
                        appendTicketsToCache(tickets, ctx) { success ->
                            if (!success)
                                Log.e("API Layer", "Failed to save tickets to cache")
                            else
                                Log.d("API Layer", "Tickets saved to cache")
                        }
                    }

                    val vouchers = jsonResponse?.getJSONArray("vouchers")
                    if (vouchers != null){
                        appendVouchersToCache(vouchers, ctx) { success ->
                            if (!success)
                                Log.e("API Layer", "Failed to save vouchers to cache")
                            else
                                Log.d("API Layer", "Vouchers saved to cache")
                        }
                    }

                    callback(true)
                }
                else -> {
                    Log.e("API Layer", "Failed to purchase tickets")
                    callback(false)
                }
            }
        }
    })
}

/**** POST REQUESTS ****/


/**** GET REQUESTS ****/


/**
 * Check if a user is registered
 * @param userId id of the user
 * @param callback callback function to handle the response
 */
fun isUserRegistered(userId: String, callback: (Boolean) -> Unit){
    val request = okhttp3.Request.Builder()
        .url("${Constants.URL}/get_user?user_id=$userId")
        .get()
        .build()

    val client = OkHttpClient()

    client.newCall(request).enqueue(object : okhttp3.Callback {
        override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
            e.printStackTrace()
            callback(false)
        }

        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
            when (response.code) {
                200 -> {
                    callback(true)
                }
                else -> {
                    callback(false)
                }
            }
        }
    })
}

/**
 * Function to fetch the user's tickets
 * @param userId id of the user
 * @param callback callback function to handle the response
 * @see Ticket
 */
fun getUserTickets(userId: String, callback: (List<Ticket>) -> Unit){
    val request = okhttp3.Request.Builder()
        .url("${Constants.URL}/tickets?user_id=$userId")
        .build()

    val client = OkHttpClient()

    client.newCall(request).enqueue(object : okhttp3.Callback {
        override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
            e.printStackTrace()
            callback(emptyList())
        }

        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
            when (response.code) {
                200 -> {
                    val responseBody = response.body?.string()
                    val jsonResponse = responseBody?.let { JSONObject(it) }
                    val tickets = jsonResponse?.getJSONArray("tickets")

                    if (tickets != null){
                        val ticketsList = mutableListOf<Ticket>()
                        for (i in 0 until tickets.length()) {
                            // get the ticket object
                            val ticket = tickets.getJSONObject(i)

                            // parse the ticket object to a Ticket object
                            ticketsList.add(parseTicket(ticket))
                        }
                        callback(ticketsList)
                    }
                }
                else -> {
                    print("Error getting tickets")
                }
            }
        }
    })
}

/**
 * Function to fetch the user's vouchers
 * @param userId id of the user
 * @param callback callback function to handle the response
 * @see Voucher
 */
fun getUserVouchers(userId: String, callback: (List<Voucher>) -> Unit){
    val request = okhttp3.Request.Builder()
        .url("${Constants.URL}/vouchers?user_id=$userId")
        .build()

    val client = OkHttpClient()

    client.newCall(request).enqueue(object : okhttp3.Callback {
        override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
            e.printStackTrace()
            callback(emptyList())
        }

        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
            when (response.code) {
                200 -> {
                    val responseBody = response.body?.string()
                    val jsonResponse = responseBody?.let { JSONObject(it) }
                    val vouchers = jsonResponse?.getJSONArray("vouchers")

                    if (vouchers != null){
                        val vouchersList = mutableListOf<Voucher>()
                        for (i in 0 until vouchers.length()) {
                            // get the voucher object
                            val voucher = vouchers.getJSONObject(i)

                            // parse the voucher object to a Voucher object
                            vouchersList.add(parseVoucher(voucher))
                        }
                        callback(vouchersList)
                    }
                }
                else -> {
                    print("Error getting vouchers")
                }
            }
        }
    })
}

/**
 * Function to fetch the theater shows
 * @param ctx context of the application
 * @param callback callback function to handle the response
 * @see Show

 */
fun getShows(ctx: Context, callback: (List<Show>) -> Unit) {
    val client = OkHttpClient()

    val areImagesCached = areImagesStoreInCache(ctx)

    var request = okhttp3.Request.Builder()
        .url("${Constants.URL}/shows")
        .get()
        .build()

    if (!areImagesCached){
        request = okhttp3.Request.Builder()
            .url("${Constants.URL}/shows?images=true")
            .get()
            .build()
    }

    client.newCall(request).enqueue(object : okhttp3.Callback {
        override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
            e.printStackTrace()
            callback(emptyList())
        }

        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
            when (response.code) {
                200 -> {
                    val responseBody = response.body?.string()
                    val jsonResponse = responseBody?.let { JSONObject(it) }
                    val shows = jsonResponse?.getJSONArray("shows") ?: return

                    val showsList = mutableListOf<Show>()
                    for (i in 0 until shows.length()) {
                        val show = shows.getJSONObject(i)

                        showsList.add(parseShow(show))
                        if (areImagesCached)
                            continue

                        val imageName = show.getString("picture")
                        val imageB64 = show.getString("picture_b64")

                        saveImageToCache(imageB64, imageName, ctx){success ->
                            if (!success)
                                Log.e("API Layer", "Failed to save image to cache")
                        }
                    }

                    callback(showsList)
                }
                else -> {
                    print("Error getting shows")
                }
            }
        }
    })
}

/**
 * Function to fetch the user's orders
 * @param userId id of the user
 * @param callback callback function to handle the response
 * @see OrderRcv
 */
fun getUserOrders(userId: String, callback: (List<OrderRcv>) -> Unit) {
    val client = OkHttpClient()

    val request = okhttp3.Request.Builder()
        .url("${Constants.URL}/orders?user_id=$userId")
        .get()
        .build()

    client.newCall(request).enqueue(object : okhttp3.Callback {
        override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
            e.printStackTrace()
            callback(emptyList())
        }

        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
            when (response.code) {
                200 -> {
                    val responseBody = response.body?.string()
                    val jsonResponse = responseBody?.let { JSONObject(it) }
                    val orders = jsonResponse?.getJSONArray("orders") ?: return

                    val ordersList = mutableListOf<OrderRcv>()
                    for (i in 0 until orders.length()) {
                        val order = orders.getJSONObject(i)

                        ordersList.add(parseOrderRcv(order))
                    }

                    callback(ordersList)
                }
                else -> {
                    print("Error getting orders")
                }
            }
        }
    })
}

/**
 * Function to fetch the user's transactions
 * @param userId id of the user
 * @param callback callback function to handle the response
 * @see Transaction
 */
fun getUserTransactions(userId: String, callback: (JSONObject) -> Unit) {
    val client = OkHttpClient()

    val request = okhttp3.Request.Builder()
        .url("${Constants.URL}/transactions?user_id=$userId")
        .get()
        .build()

    client.newCall(request).enqueue(object : okhttp3.Callback {
        override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
            e.printStackTrace()
            callback(JSONObject())
        }

        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
            when (response.code) {
                200 -> {
                    val responseBody = response.body?.string()
                    val jsonResponse = responseBody?.let { JSONObject(it) }
                    if (jsonResponse != null) {
                        callback(jsonResponse)
                    }
                }
                else -> {
                    print("Error getting transactions")
                }
            }
        }
    })
}

/**** GET REQUESTS ****/


/**** PING REQUESTS ****/
/**
 * Checks if the server is up
 * @param callback callback function to handle the response
 */
fun pingServer(callback: (Boolean) -> Unit){
    val request = okhttp3.Request.Builder()
        .url("${Constants.URL}/ping")
        .get()
        .build()

    val client = OkHttpClient()

    client.newCall(request).enqueue(object : okhttp3.Callback {
        override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
            e.printStackTrace()
            callback(false)
        }

        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
            when (response.code) {
                200 -> {
                    callback(true)
                }
                else -> {
                    callback(false)
                }
            }
        }
    })
}



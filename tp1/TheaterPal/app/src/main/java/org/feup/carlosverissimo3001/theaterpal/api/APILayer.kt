package org.feup.carlosverissimo3001.theaterpal.api

import android.content.Context
import okhttp3.OkHttpClient
import org.feup.carlosverissimo3001.theaterpal.Server
import org.feup.carlosverissimo3001.theaterpal.file.areImagesStoreInCache
import org.feup.carlosverissimo3001.theaterpal.file.saveImageToCache
import org.feup.carlosverissimo3001.theaterpal.models.Order
import org.feup.carlosverissimo3001.theaterpal.models.Show
import org.feup.carlosverissimo3001.theaterpal.models.Ticket
import org.feup.carlosverissimo3001.theaterpal.models.parseShow
import org.feup.carlosverissimo3001.theaterpal.models.Voucher
import org.feup.carlosverissimo3001.theaterpal.models.parseTicket
import org.feup.carlosverissimo3001.theaterpal.models.parseVoucher
import org.json.JSONArray
import org.json.JSONObject

fun getUserTickets(user_id: String, callback: (List<Ticket>) -> Unit){
    var request = okhttp3.Request.Builder()
        .url("${Server.URL}/tickets?user_id=$user_id")
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
                        var ticketsList = mutableListOf<Ticket>()
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

fun getUserVouchers(user_id: String, callback: (List<Voucher>) -> Unit){
    var request = okhttp3.Request.Builder()
        .url("${Server.URL}/vouchers?user_id=$user_id")
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
                        var vouchersList = mutableListOf<Voucher>()
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

fun getShows(ctx: Context, callback: (List<Show>) -> Unit) {
    val client = OkHttpClient()

    val areImagesCached = areImagesStoreInCache(ctx)

    var request = okhttp3.Request.Builder()
        .url("${Server.URL}/shows")
        .get()
        .build()

    if (!areImagesCached){
        request = okhttp3.Request.Builder()
            .url("${Server.URL}/shows?images=true")
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
                    val shows = jsonResponse?.getJSONArray("shows")

                    if (shows == null)
                        return

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
                                print("Error saving $imageName to cache")
                            else
                                print("Saved $imageName to cache")
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

fun sumbitOrder(ctx: Context, order: Order, callback: (Boolean) -> Unit){
    // TODO: Implement submitOrder
}

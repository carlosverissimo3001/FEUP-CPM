package org.feup.carlosverissimo3001.theatervalid8.api

import android.content.Context
import org.feup.carlosverissimo3001.theatervalid8.models.Show
import okhttp3.OkHttpClient
import org.feup.carlosverissimo3001.theatervalid8.Constants
import org.feup.carlosverissimo3001.theatervalid8.file.areImagesStoreInCache
import org.feup.carlosverissimo3001.theatervalid8.models.ShowDate
import org.feup.carlosverissimo3001.theatervalid8.file.saveImageToCache
import org.json.JSONArray
import org.json.JSONObject

// Handles the API calls
class APILayer (private val ctx: Context){
    private val client = OkHttpClient()

    fun fetchShows(callback: (List<Show>) -> Unit) {
        val areImagesCached = areImagesStoreInCache(ctx)

        println("Are images cached: $areImagesCached")

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
                // Pass an empty string to the callback to indicate failure
                callback(emptyList())
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val responseCode = response.code

                // 200 means the user was found
                if (responseCode == 200) {
                    val responseBody = response.body?.string()
                    val jsonResponse = responseBody?.let { JSONObject(it) }
                    // get the shows array
                    val shows = jsonResponse?.getJSONArray("shows")

                    if (!areImagesCached) {
                        shows?.let {
                            for (i in 0 until it.length()) {
                                val show = it.getJSONObject(i)
                                val imageName = show.getString("picture")
                                val imageB64 = show.getString("picture_b64")

                                saveImageToCache(imageB64, imageName, ctx) { success ->
                                    if (!success) {
                                        println("Error saving $imageName to cache")
                                    } else {
                                        println("Saved $imageName to cache")
                                    }
                                }
                            }
                        }
                    }

                    // convert the shows array to a list of Show objects
                    val showsList = shows?.let { setShows(it) }

                    // Pass the list of shows to the callback
                    if (showsList != null) {
                        callback(showsList)
                    }

                } else if (responseCode == 404) {
                    // user_id not found
                    println("User not found")
                    // Pass an empty string to the callback to indicate failure
                    callback(emptyList())
                    // TODO: handle this case, although it should never happen
                }
            }
        })
    }

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

    private fun setShows(jsonArray: JSONArray) : List<Show>{
        val shows = mutableListOf<Show>()

        for (i in 0 until jsonArray.length()) {
            val show = jsonArray.getJSONObject(i)
            val dates = show.getJSONArray("dates")
            val showDates = mutableListOf<ShowDate>()

            for (j in 0 until dates.length()) {
                val date = dates.getJSONObject(j)
                val dateString = date.getString("date")
                val showdateid = date.getInt("showdateid")

                showDates.add(ShowDate(dateString, showdateid))
            }

            shows.add(
                Show(
                    show.getInt("showid"),
                    show.getString("name"),
                    show.getString("description"),
                    show.getString("picture"),
                    show.getString("picture_b64"),
                    show.getString("releasedate"),
                    show.getInt("duration"),
                    show.getInt("price"),
                    showDates
                )
            )
        }
        return shows
    }
}
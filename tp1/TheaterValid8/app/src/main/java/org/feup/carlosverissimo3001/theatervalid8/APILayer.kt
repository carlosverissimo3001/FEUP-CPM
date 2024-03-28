package org.feup.carlosverissimo3001.theatervalid8

import org.feup.carlosverissimo3001.theatervalid8.models.Show
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONObject

// Handles the API calls
class APILayer {
    private val client = OkHttpClient()

    fun fetchShows(callback: (List<Show>) -> Unit) {
        val request = okhttp3.Request.Builder()
            .url("${Server.URL}/shows")
            .get()
            .build()


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

    fun fetchShowDetails(showId: Int)  {
        var request = okhttp3.Request.Builder()
            .url("${Server.URL}/shows/$showId")
            .get()
            .build()
    }

    private fun setShows(jsonArray: JSONArray) : List<Show>{
        val shows = mutableListOf<Show>()

        for (i in 0 until jsonArray.length()) {
            val show = jsonArray.getJSONObject(i)
            shows.add(
                Show(
                    show.getInt("showid"),
                    show.getString("name"),
                    show.getString("description"),
                    show.getString("picture"),

                    // show.getString("pictureBase64"), // TODO: Does the validator need the show image?
                    "",

                    show.getInt("price"),
                    emptyList() // The dates are only fetched when the validator selects this show
                )
            )
        }
        return shows
    }
}
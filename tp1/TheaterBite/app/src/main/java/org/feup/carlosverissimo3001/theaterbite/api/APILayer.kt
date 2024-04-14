package org.feup.carlosverissimo3001.theaterbite.api

import android.content.Context
import okhttp3.OkHttpClient
import org.feup.carlosverissimo3001.theaterbite.Constants
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
}
package org.feup.carlosverissimo3001.theaterpal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.feup.carlosverissimo3001.theaterpal.auth.Authentication
import org.json.JSONObject

class LauncherActivity : AppCompatActivity() {
    // This activity is just a launcher activity
    // It will check the keystore for a user_auth_key
    // If it exists, it will start the MainActivity
    // If it doesn't, it will start the RegisterActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if the user has authenticated before
        val rsaPairExists = Authentication(this).doesRSAKeyPairExist()

        // next activity
        var intent = Intent(this, RegisterActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

        println("RSA Pair Exists: $rsaPairExists")

        if (rsaPairExists) {
            // already authenticated, instead of RegisterActivity, start MainActivity
            intent = Intent(this, MainActivity::class.java)

            // get the user_id from the keystore
            val userId = Authentication(this).getUserID()

            // use the server to know the user's name
            getUserName(userId) { name ->
                // pass the user's name to the next activity
                intent.putExtra("name", name)
                // start the MainActivity after receiving the user's name
                startActivity(intent)
                // finish the current activity
                finish()
            }
        }

        else {
            startActivity(intent)
            finish()
        }
    }

    private fun getUserName(userId: String, callback: (String) -> Unit) {
        // get the user's name from the server
        val client = OkHttpClient()

        // add the user_id to the request body
        val requestBody = JSONObject()
        requestBody.put("user_id", userId)

        val body = requestBody.toString().toRequestBody("application/json".toMediaType())

        val request = okhttp3.Request.Builder()
            .url("${Server.URL}/get_user")
            .post(body)
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
                    // get the user's name
                    val name = jsonResponse?.getString("name")
                    // Pass the name to the callback
                    if (name != null) {
                        callback(name)
                    }
                } else if (responseCode == 404) {
                    // user_id not found
                    println("User not found")
                    // Pass an empty string to the callback to indicate failure
                    callback("")
                    // TODO: handle this case, although it should never happen
                }
            }
        })
    }

}
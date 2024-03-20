package org.feup.carlosverissimo3001.theaterpal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // TODO: This needs to be an environment variable and not hardcoded
        var url = "https://dc7c-161-230-86-77.ngrok-free.app"

        // Get the login button
        val loginButton: Button = findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            // Get the NIF from the input field
            val nif = findViewById<EditText>(R.id.nif).text.toString()

            val jsonObject = JSONObject()
            jsonObject.put("nif", nif)

            // HTTP request to the server
            val client = OkHttpClient()

            val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaType())

            val request = okhttp3.Request.Builder()
                .url("$url/pre-register")
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    val responseCode = response.code

                    // 204 means the user is not registered
                    if (responseCode == 204){
                        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent.putExtra("nif", nif)
                        startActivity(intent)
                        finish()
                    }

                    // 200 means the user is registered, show the main activity
                    else if (responseCode == 200){
                        // start main activity (home screen)
                        // TODO: Implement this
                        val responseBody = response.body?.string()
                        val jsonResponse = responseBody?.let { it1 -> JSONObject(it1) }

                        // Get the user id from the response
                        val userId = jsonResponse?.get("user_id")

                        // start main activity (home screen)
                        // TODO: Implement this
                        runOnUiThread{
                            Toast.makeText(applicationContext, "User already registered with id: $userId", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            })
        }
    }
}

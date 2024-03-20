package org.feup.carlosverissimo3001.theaterpal

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class RegisterActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        // Get the NIF from the intent
        var receivedNif = intent.getStringExtra("nif");

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Set the NIF in the input field so that the user doesn't have to type it again
        val nifEditText = findViewById<EditText>(R.id.nif)
        nifEditText.setText(receivedNif)

        var url = "https://dc7c-161-230-86-77.ngrok-free.app"

        var registerButton: Button = findViewById(R.id.registerButton)

        registerButton.setOnClickListener {
            // Get the fields
            val name = findViewById<EditText>(R.id.name).text.toString()
            val credit_card_number = findViewById<EditText>(R.id.credit_card_number).text.toString()
            val credit_card_validity_month =
                findViewById<EditText>(R.id.credit_card_validity_month).text.toString()
            val credit_card_validity_year =
                findViewById<EditText>(R.id.credit_card_validity_year).text.toString()

            val date = "$credit_card_validity_month-$credit_card_validity_year"

            val userdata = mapOf(
                "nif" to receivedNif,
                "name" to name,
                "card" to mapOf(
                    "type" to "Visa", // for now keep it hardcoded
                    "number" to credit_card_number,
                    "expiration_date" to date
                ),
                "public_key" to "public_key_test" // for now keep it hardcoded
            )

            // HTTP request to the server
            var client = OkHttpClient()

            val jsonObject = JSONObject(userdata)

            var requestBody = jsonObject.toString().toRequestBody("application/json".toMediaType())

            var request = okhttp3.Request.Builder()
                .url("$url/register")
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    val responseCode = response.code

                    // 201 means the user was successfully registered
                    if (responseCode == 201) {
                        val responseBody = response.body?.string()
                        val jsonResponse = responseBody?.let { it1 -> JSONObject(it1) }

                        // Get the user id from the response
                        val userId = jsonResponse?.get("user_id")

                        // start main activity (home screen)
                        // TODO: Implement this
                       runOnUiThread{
                            Toast.makeText(applicationContext, "User registered with id: $userId", Toast.LENGTH_LONG).show()
                       }
                    } else if (responseCode == 500) {
                        // error registering the user
                        // need to show an error message

                    }
                }
            })
        }
    }
}
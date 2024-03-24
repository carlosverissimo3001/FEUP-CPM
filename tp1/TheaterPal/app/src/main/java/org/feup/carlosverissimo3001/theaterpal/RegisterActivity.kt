package org.feup.carlosverissimo3001.theaterpal

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import org.feup.carlosverissimo3001.theaterpal.auth.Authentication
import org.json.JSONObject

class RegisterActivity : AppCompatActivity(){
    /*fun setSpinners(){
        val months = (1..12).map { it.toString().padStart(2, '0') }
        val years = (2024..2044).map { it.toString() }
        val monthAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, months)
        val yearAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, years)

        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val monthSpinner = findViewById<Spinner>(R.id.month_spinner)
        monthSpinner.adapter = monthAdapter

        val yearSpinner = findViewById<Spinner>(R.id.year_spinner)
        yearSpinner.adapter = yearAdapter
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
       /* setSpinners()*/

        var auth = Authentication(this)

        var registerButton: Button = findViewById(R.id.registerButton)

        registerButton.setOnClickListener {
            // Get the fields
            val name = findViewById<EditText>(R.id.name).text.toString()
            val credit_card_number = findViewById<EditText>(R.id.credit_card_number).text.toString()
            val nif = findViewById<EditText>(R.id.nif).text.toString()
            val credit_card_validity_month =
                findViewById<EditText>(R.id.credit_card_validity_month).text.toString()
            val credit_card_validity_year =
                findViewById<EditText>(R.id.credit_card_validity_year).text.toString()
            val date = "$credit_card_validity_month-$credit_card_validity_year"

            // authenticate the user and store the public key
            auth.generateRSAKeyPair()

            // retrieve the public key
            val publicKey = auth.getPublicKey()

            println("Public key: $publicKey")

            val userdata = mapOf(
                "nif" to nif,
                "name" to name,
                "card" to mapOf(
                    "type" to "Visa", // for now keep it hardcoded
                    "number" to credit_card_number,
                    "expiration_date" to date
                ),
                "public_key" to publicKey
            )

            // HTTP request to the server
            var client = OkHttpClient()

            val jsonObject = JSONObject(userdata)

            var requestBody = jsonObject.toString().toRequestBody("application/json".toMediaType())

            var request = okhttp3.Request.Builder()
                .url("${Server.URL}/register")
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

                        // save the user id in the Android KeyStore
                        auth.storeUserID(userId.toString())

                        // start main activity (home screen)
                        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

                        // greet the user on the main activity
                        intent.putExtra("name", name)

                        startActivity(intent)
                        finish()

                    } else if (responseCode == 500) {
                        // error registering the user
                        // need to show an error message

                    }
                }
            })
        }
    }
}
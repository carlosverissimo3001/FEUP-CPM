package org.feup.carlosverissimo3001.theaterpal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.feup.carlosverissimo3001.theaterpal.api.getUserName
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
        var rsaPairExists = Authentication(this).doesRSAKeyPairExist()

        // If the userid exists in the phone, it has been created by the server
        var serverACK = Authentication(this).getUserID() != ""

        // next activity
        var intent = Intent(this, RegisterActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

        // ** TO NOT HAVE TO WRITE EVERY FIELD WHEN DEVELOPING **
        //rsaPairExists = true
       // serverACK = true
        // ** TO NOT HAVE TO WRITE EVERY FIELD WHEN DEVELOPING **

        if (rsaPairExists && serverACK) {
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
}
package org.feup.carlosverissimo3001.theaterpal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import org.feup.carlosverissimo3001.theaterpal.api.isUserRegistered
import org.feup.carlosverissimo3001.theaterpal.api.pingServer
import org.feup.carlosverissimo3001.theaterpal.auth.Authentication
import org.feup.carlosverissimo3001.theaterpal.file.deleteCache

class LauncherActivity : AppCompatActivity() {
    // This activity is just a launcher activity
    // It will check the keystore for a user_auth_key
    // If it exists, it will start the MainActivity
    // If it doesn't, it will start the RegisterActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context = this

        // Ping the server to check if it is up
        var serverPinged = false

        // Launch the ping request asynchronously
        pingServer { isUp ->
            // Update the UI on the main thread
            runOnUiThread {
                // Server is down
                if (!isUp) {
                    // Start the ServerDown activity
                    val intent = Intent(context, ServerDown::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                } else {
                    // Server is up

                    // Check if the user has authenticated before
                    val rsaPairExists = Authentication(this).doesRSAKeyPairExist()

                    // If the userid exists in the phone, check if it exists on the server
                    var serverACK = false
                    var serverVerified = false

                    // Read the userid from the local storage
                    val userId = Authentication(this).getUserID()

                    // Use the userId to check if the user is registered on the server
                    if (userId != "")
                        isUserRegistered(userId) {
                            serverACK = it
                            serverVerified = true
                        }

                    while (!serverVerified)
                        Thread.sleep(100)

                    // next activity
                    var intent = Intent(this, RegisterActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

                    if (rsaPairExists && serverACK) {
                        // already authenticated, instead of RegisterActivity, start MainActivity
                        intent = Intent(this, MainActivity::class.java)

                        // start the MainActivity
                        startActivity(intent)

                        // finish the current activity
                        finish()
                    }

                    // Not authenticated, start RegisterActivity
                    else {
                        // clear cache
                        // Why? Because if the server deletes the user, the app will still have the user's data (tickets, etc)
                        // And, even with a new user id, the app will still have the old user's data
                        var isCacheDeleted = false
                        deleteCache(applicationContext){
                            if (!it){
                                Log.e("LauncherActivity", "Failed to delete cache")
                            }
                            else {
                                isCacheDeleted = true
                            }
                        }

                        while (!isCacheDeleted)
                            Thread.sleep(100)

                        // Start the RegisterActivity
                        startActivity(intent)

                        // finish the current activity
                        finish()
                    }
                }
            }
        }
    }
}
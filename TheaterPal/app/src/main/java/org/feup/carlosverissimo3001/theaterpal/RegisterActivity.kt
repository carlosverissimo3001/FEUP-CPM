package org.feup.carlosverissimo3001.theaterpal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import org.feup.carlosverissimo3001.theaterpal.api.registerUser
import org.feup.carlosverissimo3001.theaterpal.auth.Authentication
import org.feup.carlosverissimo3001.theaterpal.models.User
import org.feup.carlosverissimo3001.theaterpal.screens.Register
import org.feup.carlosverissimo3001.theaterpal.screens.fragments.register.TopBar

class RegisterActivity : AppCompatActivity(){
    private var auth = Authentication(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BottomBarTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    CenteredContent {
                        TopBar()

                        Register(
                            onSubmit = { user ->
                                handleButtonClick(user)
                            }
                        )
                    }
                }
            }
        }
    }

    private fun handleButtonClick(user: User){
        // create a new RSA key pair
        auth.generateRSAKeyPair()

        // get the public key
        user.publicKey = auth.getPublicKeyB64()

        Log.d("RegisterActivity", "USER: $user")

        registerUser(applicationContext, user, callback = {success, userid ->
            if (success) {
                println("User registered successfully")
                auth.storeUserID(userid)
                auth.storeUserNIF(user.nif)
                auth.storeUserName(user.name)

                // start main activity (home screen)
                val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

                // greet the user on the main activity
                intent.putExtra("name", user.name)

                startActivity(intent)
                finish()
            } else {
                // show an error message
            }
        })
    }
}


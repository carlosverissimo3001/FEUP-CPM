package org.feup.carlosverissimo3001.theaterpal.auth

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import org.feup.carlosverissimo3001.theaterpal.Crypto
import java.security.KeyPairGenerator
import java.security.KeyStore

class Authentication (private val context: Context){
    fun generateRSAKeyPair() {
        try {
            val spec = KeyGenParameterSpec.Builder(
                Crypto.KEY_NAME,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setKeySize(Crypto.KEY_SIZE)
                .setDigests(KeyProperties.DIGEST_NONE, KeyProperties.DIGEST_SHA256)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                // the other fields in the demo crypto project are also for signing
                // but we only need encryption here (i think)
                .build()

            KeyPairGenerator.getInstance(Crypto.KEY_ALGO, Crypto.ANDROID_KEYSTORE).apply {
                initialize(spec)
                generateKeyPair() // the generated key pair is stored in the Android KeyStore
            }
        }
        catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    /**
     * Get the public key from the Android KeyStore
     * @return the public key in Base64
     */
    fun getPublicKey(): String {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        val publicKey = keyStore.getCertificate(Crypto.KEY_NAME).publicKey

        val publicKeyBytes = publicKey.encoded

        return Base64.encodeToString(publicKeyBytes, Base64.DEFAULT)
    }

    // Receive the user_id from the server and store it in the Android KeyStore
    // The user ID is a uuid with 16 bytes (6-2-2-2-6)
    fun storeUserID(user_id: String) {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)


        val editor = context.getSharedPreferences("user", Context.MODE_PRIVATE).edit()
        editor.putString("user_id", user_id)
        editor.apply()
    }
    
    fun getUserID(): String {
        val sharedPref = context.getSharedPreferences("user", Context.MODE_PRIVATE)
        return sharedPref.getString("user_id", "")!!
    }

    fun doesRSAKeyPairExist(): Boolean {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        return keyStore.containsAlias(Crypto.KEY_NAME)
    }
}
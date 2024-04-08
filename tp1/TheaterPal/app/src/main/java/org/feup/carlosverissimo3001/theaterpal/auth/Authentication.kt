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
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT or KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
            )
                .setKeySize(Crypto.KEY_SIZE)
                .setDigests(KeyProperties.DIGEST_NONE, KeyProperties.DIGEST_SHA256)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
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
        val keyStore = KeyStore.getInstance(Crypto.ANDROID_KEYSTORE)
        keyStore.load(null)
        val publicKey = keyStore.getCertificate(Crypto.KEY_NAME).publicKey

        val publicKeyBytes = publicKey.encoded

        return Base64.encodeToString(publicKeyBytes, Base64.DEFAULT)
    }

    // Receive the user_id from the server and store it in the Android KeyStore
    // The user ID is a uuid with 16 bytes (6-2-2-2-6)
    fun storeUserID(userId: String) {
        context.openFileOutput("user_id", Context.MODE_PRIVATE).use {
            it.write(userId.toByteArray())
        }
    }

    fun storeUserNIF(nif: String) {
        context.openFileOutput("user_nif", Context.MODE_PRIVATE).use {
            it.write(nif.toByteArray())
        }
    }
    
    fun getUserID(): String {
        val file = context.getFileStreamPath("user_id")
        if (file.exists()) {
            return file.readText()
        }

        return ""
    }

    fun getUserNIF(): String {
        val file = context.getFileStreamPath("user_nif")
        if (file.exists()) {
            return file.readText()
        }

        return ""
    }

    fun doesRSAKeyPairExist(): Boolean {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        return keyStore.containsAlias(Crypto.KEY_NAME)
    }
}
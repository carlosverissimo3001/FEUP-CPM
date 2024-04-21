package org.feup.carlosverissimo3001.theatervalid8.auth

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import org.feup.carlosverissimo3001.theatervalid8.Constants
import java.math.BigInteger
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.util.Calendar
import java.util.GregorianCalendar
import javax.security.auth.x500.X500Principal
import java.security.Signature

class Authentication (private val context: Context){
    /**
     * Signs a given content with the private key stored in the Android KeyStore.
     * @param content The content to be signed (NOTE, the content is not changed, only the signature is returned).
     * @return The signature of the content.
     */
    fun sign(content: String) : ByteArray {
        var result = ByteArray(0)

        if (content.isEmpty())
            return (ByteArray(0))

        try {
            val entry = KeyStore.getInstance(Constants.ANDROID_KEYSTORE).run {
                load(null)
                getEntry(Constants.KEY_NAME, null)
            }
            val privateKey = (entry as KeyStore.PrivateKeyEntry).privateKey
            val signature = Signature.getInstance(Constants.SIGN_ALGO).apply {
                initSign(privateKey)
                update(content.toByteArray())
                // sign(....)
            }
            val signedBytes = signature.sign()
            Log.d("Encryption", "Signature size = $signedBytes bytes.")
            result = signedBytes

        } catch (ex: Exception) {
            Log.d("Auth", ex.toString())
        }

        return result
    }
}


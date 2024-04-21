package org.feup.carlosverissimo3001.theaterpal.auth

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import org.feup.carlosverissimo3001.theaterpal.Constants
import java.math.BigInteger
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
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
            Log.d("Auth", "Signature size = $signedBytes bytes.")
            result = signedBytes

        } catch (ex: Exception) {
            Log.d("Auth", ex.toString())
        }

        return result
    }


    fun generateRSAKeyPair() {
        try {
            val entry = KeyStore.getInstance(Constants.ANDROID_KEYSTORE).run {
                load(null)
                getEntry(Constants.KEY_NAME, null)
            }
            if (entry == null) {
                val spec = KeyGenParameterSpec.Builder(Constants.KEY_NAME, KeyProperties.PURPOSE_SIGN)
                    .setKeySize(Constants.KEY_SIZE)
                    .setDigests(KeyProperties.DIGEST_SHA256)
                    .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
                    .setCertificateSubject(X500Principal("CN=" + Constants.KEY_NAME))
                    .setCertificateSerialNumber(BigInteger.valueOf(12131415L))
                    .setCertificateNotBefore(GregorianCalendar().time)
                    .setCertificateNotAfter(GregorianCalendar().apply { add(Calendar.YEAR, 10) }.time)
                    .build()
                KeyPairGenerator.getInstance(Constants.KEY_ALGO, Constants.ANDROID_KEYSTORE).run {
                    initialize(spec)
                    generateKeyPair()
                }
            }
        }
        catch (ex: Exception) {
            Log.d("Auth", ex.toString())
        }
    }

    /**
     * Get the public key from the Android KeyStore
     * @return the public key in Base64
     */
    fun getPublicKeyB64(): String {
        val keyStore = KeyStore.getInstance(Constants.ANDROID_KEYSTORE)
        keyStore.load(null)
        val publicKey = keyStore.getCertificate(Constants.KEY_NAME).publicKey

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

    fun storeUserName(name: String) {
        context.openFileOutput("user_name", Context.MODE_PRIVATE).use {
            it.write(name.toByteArray())
        }
    }

    fun getUserName(): String {
        val file = context.getFileStreamPath("user_name")
        if (file.exists()) {
            return file.readText()
        }

        return ""
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
        return keyStore.containsAlias(Constants.KEY_NAME)
    }
}


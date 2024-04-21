package org.feup.carlosverissimo3001.theaterpal.api

import android.util.Log
import org.feup.carlosverissimo3001.theaterpal.Constants
import org.feup.carlosverissimo3001.theaterpal.nfc.logTag
import java.security.KeyStore
import java.security.Signature
import javax.crypto.Cipher

fun encrypt(content: String) : ByteArray {
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
		Log.d(logTag, "Signature size = $signedBytes bytes.")
		result = signedBytes
		
	} catch (ex: Exception) {
		Log.d(logTag, ex.toString())
	}

	return result
}
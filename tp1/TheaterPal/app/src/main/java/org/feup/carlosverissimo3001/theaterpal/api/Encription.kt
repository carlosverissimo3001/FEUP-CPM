package org.feup.carlosverissimo3001.theaterpal.api

import org.feup.carlosverissimo3001.theaterpal.Crypto
import java.security.KeyStore
import javax.crypto.Cipher

fun encrypt(content: String) : ByteArray {
	var result = ByteArray(0)

	if (content.isEmpty())
		return (ByteArray(0))

	val privateKey = KeyStore.getInstance(Crypto.ANDROID_KEYSTORE).run {
		load(null)
		getKey(Crypto.KEY_NAME, null)
	}

	try {
		result = Cipher.getInstance(Crypto.KEY_ALGO).run {
			init(Cipher.ENCRYPT_MODE, privateKey)
			doFinal(content.toByteArray())
		}
	}
	catch (e: Exception) {
		e.printStackTrace()
	}
	return (result)
}
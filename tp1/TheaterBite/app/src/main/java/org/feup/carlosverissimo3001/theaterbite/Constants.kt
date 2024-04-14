package org.feup.carlosverissimo3001.theaterbite

import android.nfc.NfcAdapter
import android.util.Base64
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import java.text.SimpleDateFormat

const val READER_FLAGS = NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK

object Constants {
    const val URL = "https://open-blowfish-cleanly.ngrok-free.app"
    const val UUID_SIZE = 36
    const val KEY_SIZE = 512
    const val SIGN_ALGO = "SHA256WithRSA"
}

val poppinsFontFamily = androidx.compose.ui.text.font.FontFamily(
    Font(R.font.poppins_light, FontWeight.W300),
    Font(R.font.poppins_regular, FontWeight.W400),
    Font(R.font.poppins_medium, FontWeight.W500),
    Font(R.font.poppins_semibold, FontWeight.W600),
    Font(R.font.poppins_bold, FontWeight.W700),
    Font(R.font.poppins_black, FontWeight.W900),
)


/* Utility top-level function */
fun byteArrayToHex(ba: ByteArray): String {
    val sb = StringBuilder(ba.size * 2)
    for (b in ba) sb.append(String.format("%02x", b))
    return sb.toString()
}

fun hexStringToByteArray(s: String): ByteArray {
    val data = ByteArray(s.length/2)
    for (k in 0 until s.length/2)
        data[k] = ((Character.digit(s[2*k], 16) shl 4) + Character.digit(s[2*k+1], 16)).toByte()
    return data
}

fun americanDateToNormal(date: String): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    val date = formatter.parse(date)
    val formatter2 = SimpleDateFormat("dd/MM/yyyy")
    return formatter2.format(date)
}

fun decodePublicKey(base64PublicKey: String): PublicKey {
    // Decode the Base64 string into a byte array
    val publicKeyBytes = Base64.decode(base64PublicKey, Base64.DEFAULT)

    // Create an X509EncodedKeySpec using the byte array
    val keySpec = X509EncodedKeySpec(publicKeyBytes)

    // Initialize a KeyFactory for RSA
    val keyFactory = KeyFactory.getInstance("RSA")

    // Generate the public key from the X509EncodedKeySpec
    return keyFactory.generatePublic(keySpec)
}
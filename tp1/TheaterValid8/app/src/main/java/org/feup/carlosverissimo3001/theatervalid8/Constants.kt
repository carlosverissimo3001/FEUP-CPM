package org.feup.carlosverissimo3001.theatervalid8

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object Constants {
    const val KEY_SIZE = 512
    const val ANDROID_KEYSTORE = "AndroidKeyStore"
    const val KEY_ALGO = "RSA"
    const val SIGN_ALGO = "SHA256WithRSA"
    const val KEY_NAME = "user_auth_key"
    const val URL = "https://open-blowfish-cleanly.ngrok-free.app"
    /* const val URL = "https://wrongly-in-pug.ngrok-free.app" */
    const val ACTION_CARD_DONE = "CMD_PROCESSING_DONE"
}

val showNameImageMap = mutableMapOf<String, String>()

@Composable
fun CenteredContent(content: @Composable () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        content()
    }
}

@Composable
fun TopCenteredContent(content: @Composable () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.fillMaxSize()
    ) {
        content()
    }
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

fun formatDate(inputDate: String): String {
    // Parse the input date string
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.parse(inputDate, formatter)

    var dayOfWeek = date.dayOfWeek.toString().lowercase()
    dayOfWeek = toTitleCase(dayOfWeek)

    val day = date.dayOfMonth.toString()

    var month = date.month.toString().lowercase()
    month = toTitleCase(month)

    val daySuffix = when {
        day.toInt() in 11..13 -> "th"
        day.last() == '1' -> "st"
        day.last() == '2' -> "nd"
        day.last() == '3' -> "rd"
        else -> "th"
    }

    // Format the output string
    return "$dayOfWeek · $day$daySuffix of $month · 21:30h"
}

fun toTitleCase(input: String): String {
    return input.replaceFirstChar { it.titlecase() }
}
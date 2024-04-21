package org.feup.carlosverissimo3001.theaterpal

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import org.feup.carlosverissimo3001.theaterpal.file.loadImageFromCache
import java.security.PrivateKey
import java.security.Signature
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/***** UTILS.KT *****/
// Description: Helper functions for many purposes
/***** UTILS.KT *****/

/**
 * Formats a string date to the format "Day · Day of Month · hour"
 * NOTE: Hour is hardcoded to 21:30h
 * @param inputDate input date string
 * @return formatted date string
 */
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


/**
 * Convert a ByteArray to a hexadecimal string
 * @param ba ByteArray to convert
 * @return hexadecimal string
 */
fun byteArrayToHex(ba: ByteArray): String {
    val sb = StringBuilder(ba.size * 2)
    for (b in ba) sb.append(String.format("%02x", b))
    return sb.toString()
}

/**
 * Convert a hexadecimal string to a ByteArray
 * @param s hexadecimal string to convert
 * @return ByteArray
 */
fun hexStringToByteArray(s: String): ByteArray {
    val data = ByteArray(s.length/2)
    for (k in 0 until s.length/2)
        data[k] = ((Character.digit(s[2*k], 16) shl 4) + Character.digit(s[2*k+1], 16)).toByte()
    return data
}


/**
 * Composable function to display the state of a voucher/ticket order
 * @param isUsed boolean indicating if the voucher/ticket is used
 * @return Composable Text element
 */
@Composable
fun ParseIsUsed (isUsed: Boolean) {
    return if (isUsed)
        Text (
            text = "Used",
            style = TextStyle(
                color = Color.Red,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                fontFamily = marcherFontFamily
            )
        )
    else
        Text (
            text = "Active",
            style = TextStyle(
                color = Color.Green,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                fontFamily = marcherFontFamily
            )
        )
}

/**
 * Composable function that displays its contents centered within the screen
 * @param content content to display
 * @return Composable Column element
 */
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

/**
 * Composable function to display the state of an order
 * @param state state of the order
 * @return Composable Text element

 */
@Composable
fun ParseOrderState(state: String) {
    return Text(
        text = state,
        style = TextStyle(
            color = when (state) {
                "Accepted" -> Color(android.graphics.Color.parseColor("#006400"))
                "Preparing" -> Color.Yellow
                "Ready" -> Color.Cyan
                "Collected" -> Color.Green
                else -> Color.White
            },
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            fontFamily = marcherFontFamily
        )
    )
}

/**
 * Capitalizes the first letter of a string
 * @param input input string
 * @return title case string
 */
fun toTitleCase(input: String): String {
    return input.replaceFirstChar { it.titlecase() }
}


/**
 * Returns the bitmap representation of the show image
 * @param showname name of the show
 * @param ctx context of the application
 * @return bitmap representation of the show image
 */
fun fetchShowImage(showname: String, ctx: Context): Bitmap? {
    val imageName = showNameImageMap[showname]

    return loadImageFromCache(imageName!!, ctx)
}


/**
 * Format a price into a string with 2 decimal places and the currency symbol
 * @param price price to format
 * @return formatted price
 */
fun formatPrice (price: Double) : String {
    // NOTE: Locale.US is used to ensure the decimal separator is a dot
    return String.format(Locale.US, "%.2f", price) + "€"
}

/**
 * Define extension function to get a parcelable extra from an Intent
 * @param key key of the parcelable extra
 * @return parcelable extra
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    // Use the new getParcelableExtra method that accepts a class parameter
    Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)

    // else use the deprecated getParcelableExtra method
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

/**
 * Define extension function to get a parcelable array list extra from an Intent
 * @param identifierParameter key of the parcelable array list extra
 * @return parcelable array list extra
 */
@Suppress("DEPRECATION")
inline fun <reified T: Parcelable>Intent.getParcelableArrayListExtraProvider(identifierParameter: String): java.util.ArrayList<T>? {
    // If the device is running SDK 33 or higher, use the new getParcelableArrayListExtra method
    return if (Build.VERSION.SDK_INT >= 33) {
        this.getParcelableArrayListExtra(identifierParameter, T::class.java)
    }
    // Otherwise, use the deprecated getParcelableArrayListExtra method
    else {
        this.getParcelableArrayListExtra(identifierParameter)
    }
}

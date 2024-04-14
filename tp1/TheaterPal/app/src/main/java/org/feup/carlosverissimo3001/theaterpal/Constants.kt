package org.feup.carlosverissimo3001.theaterpal

import android.graphics.Color.parseColor
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.feup.carlosverissimo3001.theaterpal.models.Ticket
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object Constants {
    const val KEY_SIZE = 512
    const val ANDROID_KEYSTORE = "AndroidKeyStore"
    const val KEY_ALGO = "RSA"
    const val SIGN_ALGO = "SHA256WithRSA"
    const val KEY_NAME = "user_auth_key"
    const val URL = "https://open-blowfish-cleanly.ngrok-free.app"
    /* const val URL = "https://wrongly-in-pug.ngrok-free.app" */
    const val ACTION_CARD_DONE = "CMD_PROCESSING_DONE"
    const val UUID_SIZE = 36
}

object MyColors
{
    val tertiaryColor: Color = Color(0xFFF54242)
    val bottomNavBarUnselectedItemColor: Color = Color(0x88FFFFFF)
}


val marcherFontFamily: FontFamily = FontFamily(
    Font(R.font.marcher_light, FontWeight.Light),
    Font(R.font.marcher_bold, FontWeight.Bold),
)

// AUXILIARY FUNCTIONS
fun groupTickets(tickets: List<Ticket>) : List<Ticket>{
    // If the tickets are for the same show, group them together

    var groupedTickets = mutableListOf<Ticket>()

    for (ticketI in tickets){
        if (groupedTickets.isEmpty()){
            groupedTickets.add(ticketI)
        } else {
            var found = false
            for (ticketJ in groupedTickets){
                if (ticketI.showName == ticketJ.showName && ticketI.date == ticketJ.date && ticketI.isUsed == ticketJ.isUsed){
                    // Add ticketI to the group
                    ticketJ.numTickets += 1
                    found = true
                    break
                }
            }
            if (!found){
                groupedTickets.add(ticketI)
            }
        }
    }

    return groupedTickets
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

fun String.capitalized(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase())
            it.titlecase(Locale.getDefault())
        else it.toString()
    }
}

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
fun ParseOrderState(state: String) {
    val states = arrayOf("Accepted", "Preparing", "Ready", "Collected")

    return Text(
        text = state,
        style = TextStyle(
            color = when (state) {
                "Accepted" -> Color(parseColor("#006400"))
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
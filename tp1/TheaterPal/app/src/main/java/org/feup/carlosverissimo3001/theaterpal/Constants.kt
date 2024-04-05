package org.feup.carlosverissimo3001.theaterpal

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.feup.carlosverissimo3001.theaterpal.models.Ticket
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object Crypto {
    const val KEY_SIZE = 512
    const val ANDROID_KEYSTORE = "AndroidKeyStore"
    const val KEY_ALGO = "RSA"
    const val KEY_NAME = "user_auth_key"
}

object Server {
//    const val URL = "https://16b2-193-136-33-100.ngrok-free.app"
    const val URL = "https://wrongly-in-pug.ngrok-free.app"
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
                if (ticketI.showName == ticketJ.showName && ticketI.date == ticketJ.date){
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

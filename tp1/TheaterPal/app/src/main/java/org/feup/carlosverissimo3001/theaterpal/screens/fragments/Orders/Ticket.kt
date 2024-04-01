package org.feup.carlosverissimo3001.theaterpal.screens.fragments.Orders

import android.graphics.Bitmap
import android.graphics.Color.parseColor
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import org.feup.carlosverissimo3001.theaterpal.models.Ticket
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.Dp
import org.feup.carlosverissimo3001.theaterpal.R
import org.feup.carlosverissimo3001.theaterpal.file.loadImageFromCache
import org.feup.carlosverissimo3001.theaterpal.marcherFontFamily
import java.text.DateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@Composable
fun Dp.dpToPx() = with(LocalDensity.current) { this@dpToPx.toPx() }

@Composable
fun Ticket(ticket: Ticket, image: Bitmap?) {
    Box(
        modifier = Modifier
            .padding(10.dp)
            .background(
                color = Color(parseColor("#36363e")),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.padding(12.dp).fillMaxWidth(0.65f)
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = ticket.numTickets.toString(),
                        style = TextStyle(
                            color = Color.White,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            fontFamily = marcherFontFamily
                        )
                    )
                    Image(
                        painter = painterResource(id = R.drawable.tickets_icon),
                        contentDescription = "Ticket Icon",
                        modifier = Modifier
                            .height(20.dp)
                            .padding(end = 8.dp, start = 2.dp)
                    )
                    Text(
                        text = ticket.showName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            fontFamily = marcherFontFamily
                        )
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = formatDate(ticket.date),
                    style = TextStyle(
                        color = Color.LightGray,
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        fontFamily = marcherFontFamily
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Casa da Música",
                    style = TextStyle(
                        color = Color.LightGray,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        fontFamily = marcherFontFamily
                    )
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = "Sala VIP · ${ticket.seat}",
                    style = TextStyle(
                        color = Color.LightGray,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        fontFamily = marcherFontFamily
                    )
                )

            }
            Column(
                modifier = Modifier
                    .width(120.dp)
                    .height(120.dp),
                horizontalAlignment = Alignment.End
            ) {
                if (image != null) {
                    Image(
                        bitmap = image.asImageBitmap(),
                        contentDescription = "Ticket Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(16.dp))
                    )
                }
            }

        }
    }
}

fun formatDate(inputDate: String): String {
    // Parse the input date string
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.parse(inputDate, formatter)

    var dayOfWeek = date.dayOfWeek.toString().lowercase()
    dayOfWeek = toTitleCase(dayOfWeek)
    
    println("Day of week: $dayOfWeek")

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
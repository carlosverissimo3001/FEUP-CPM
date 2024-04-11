package org.feup.carlosverissimo3001.theaterpal.screens.fragments.Wallet.Tickets

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.feup.carlosverissimo3001.theaterpal.R
import org.feup.carlosverissimo3001.theaterpal.formatDate
import org.feup.carlosverissimo3001.theaterpal.marcherFontFamily
import org.feup.carlosverissimo3001.theaterpal.models.Ticket

@Composable
fun TicketValidate(
    ticket: Ticket,
    image: Bitmap?,
) {
    /*val (isSelected, setSelected) = remember { mutableStateOf(false) }*/

    Box(
        modifier = Modifier
            .padding(vertical = 5.dp)
            .background(
                color = Color(android.graphics.Color.parseColor("#36363e")),
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(16.dp)
            )
            .size(75.dp)
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()/*.padding(horizontal = 15.dp)*/,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(7.dp)
                    .fillMaxWidth(0.65f)
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    /*Text(
                        text = ticket.numTickets.toString(),
                        style = TextStyle(
                            color = androidx.compose.ui.graphics.Color.White,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            fontFamily = marcherFontFamily
                        )
                    )*/
                    Image(
                        painter = painterResource(id = R.drawable.tickets_icon),
                        contentDescription = "Ticket Icon",
                        modifier = Modifier
                            .height(10.dp)
                            .padding(end = 8.dp, start = 2.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = ticket.showName,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = TextStyle(
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                fontFamily = marcherFontFamily
                            )
                        )

                        Text(
                            text = "ID: ${ticket.ticketid.substring(0, 8)}...",
                            style = TextStyle(
                                color = Color.LightGray,
                                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                fontFamily = marcherFontFamily
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = formatDate(ticket.date),
                    style = TextStyle(
                        color = androidx.compose.ui.graphics.Color.LightGray,
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        fontFamily = marcherFontFamily
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Casa da Música",
                        style = TextStyle(
                            color = androidx.compose.ui.graphics.Color.LightGray,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            fontFamily = marcherFontFamily
                        )
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "Sala VIP · ${ticket.seat}",
                        style = TextStyle(
                            color = Color.LightGray,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            fontFamily = marcherFontFamily,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }


            }
            Column(
                modifier = Modifier
                    .size(75.dp),
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
package org.feup.carlosverissimo3001.theatervalid8.screens.fragments

import android.content.Context
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
import org.feup.carlosverissimo3001.theatervalid8.R
import org.feup.carlosverissimo3001.theatervalid8.api.APILayer
import org.feup.carlosverissimo3001.theatervalid8.formatDate
import org.feup.carlosverissimo3001.theatervalid8.models.Ticket
import org.feup.carlosverissimo3001.theatervalid8.poppinsFontFamily

@Composable
fun TicketValidate(
    ctx: Context,
    ticket: Ticket/*,
    image: Bitmap?*/
) {
    val image = APILayer(ctx).fetchShowImage(ticket.showName)

    Box(
        modifier = Modifier
            .padding(vertical = 5.dp)
            .background(
                color = Color(android.graphics.Color.parseColor("#36363e")),
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 3.dp,
                color = if (ticket.isValidated) Color.Green else Color.Red,
                shape = RoundedCornerShape(16.dp)
            )
            .size(100.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth(0.65f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.tickets_icon),
                            contentDescription = "Ticket Icon",
                            modifier = Modifier
                                .height(10.dp)
                                .padding(end = 8.dp, start = 2.dp)
                        )
                        Text(
                            text = ticket.showName,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = TextStyle(
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                fontFamily = poppinsFontFamily
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = formatDate(ticket.date),
                        style = TextStyle(
                            color = Color.LightGray,
                            fontSize = MaterialTheme.typography.bodySmall.fontSize,
                            fontFamily = poppinsFontFamily
                        )
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = "ID: ${ticket.ticketid.substring(0, 8)}...",
                        style = TextStyle(
                            color = Color.LightGray,
                            fontSize = MaterialTheme.typography.bodySmall.fontSize,
                            fontFamily = poppinsFontFamily
                        )
                    )
                    if(ticket.isValidated) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text(
                                text = "Your seat is: ",
                                style = TextStyle(
                                    color = Color.LightGray,
                                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                    fontFamily = poppinsFontFamily
                                )
                            )

                            Text(
                                text = ticket.seat,
                                style = TextStyle(
                                    color = Color.LightGray,
                                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text(
                                text = "Enjoy the show!",
                                style = TextStyle(
                                    color = Color.LightGray,
                                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                    else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround,
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text(
                                text = "Reason: ",
                                style = TextStyle(
                                    color = Color.LightGray,
                                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                    fontFamily = poppinsFontFamily
                                )
                            )


                            Text(
                                text = ticket.stateDesc,
                                style = TextStyle(
                                    color = Color.LightGray,
                                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .size(75.dp).align(Alignment.Top)
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
}
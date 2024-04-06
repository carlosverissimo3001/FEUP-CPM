package org.feup.carlosverissimo3001.theaterpal.screens.fragments.Wallet

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import org.feup.carlosverissimo3001.theaterpal.ParseIsUsed
import org.feup.carlosverissimo3001.theaterpal.ParseOrderState
import org.feup.carlosverissimo3001.theaterpal.R
import org.feup.carlosverissimo3001.theaterpal.formatDate
import org.feup.carlosverissimo3001.theaterpal.marcherFontFamily
import org.feup.carlosverissimo3001.theaterpal.models.OrderRcv
import org.feup.carlosverissimo3001.theaterpal.models.Ticket

@Composable
fun Order(order: OrderRcv, ctx: Context) {
    Box(
        modifier = Modifier
            .padding(10.dp)
            .background(
                color = Color(android.graphics.Color.parseColor("#36363e")),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(
                onClick = { println("Clicked on an order") }
            )
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(0.65f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Order Number: ${order.order_number}",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            fontFamily = marcherFontFamily,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                if (order.state == "Preparing") {
                    Text(
                        text = "Estimated time: 15 minutes",
                        style = TextStyle(
                            color = Color.LightGray,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            fontFamily = marcherFontFamily
                        )
                    )
                }

                // only when state is preparing or ready
                if (order.state == "Preparing" || order.state == "Ready") {
                    Text(
                        text = "(Please look for your order number in the caferia screen)",
                        style = TextStyle(
                            color = Color.LightGray,
                            fontSize = MaterialTheme.typography.bodySmall.fontSize,
                            fontFamily = marcherFontFamily
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Total : ${order.total}€",
                        style = TextStyle(
                            color = Color.LightGray,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            fontFamily = marcherFontFamily,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    ParseOrderState(order.state)
                }


                Spacer(modifier = Modifier.height(3.dp))

                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ){
                    Text(
                        text = "You used " + order.vouchers_used_cnt.toString() + (if (order.vouchers_used_cnt != 1 ) " vouchers" else " voucher") + " in this order",
                        style = TextStyle(
                            color = Color.LightGray,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            fontFamily = marcherFontFamily
                        )
                    )
                }
            }

            /*VerticalDivider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier
                    .height(100.dp)
            )
*/
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Your order contains:",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        fontFamily = marcherFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                for (item in order.items) {
                    Text(
                        text = "${item.quantity} x ${item.item_name}",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            fontFamily = marcherFontFamily
                        )
                    )
                }
            }
        }
    }
}


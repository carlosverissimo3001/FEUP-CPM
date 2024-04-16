package org.feup.carlosverissimo3001.theaterpal.screens.fragments.Wallet.Transactions

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.feup.carlosverissimo3001.theaterpal.ParseOrderState
import org.feup.carlosverissimo3001.theaterpal.auth.Authentication
import org.feup.carlosverissimo3001.theaterpal.marcherFontFamily
import org.feup.carlosverissimo3001.theaterpal.models.OrderRcv
import org.feup.carlosverissimo3001.theaterpal.models.Transaction
import org.feup.carlosverissimo3001.theaterpal.screens.fragments.Cafeteria.formatPrice

@Composable
fun Transaction(
    ctx: Context,
    transaction: Transaction,
    onClick: (Transaction) -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(10.dp)
            .background(
                color = Color(android.graphics.Color.parseColor("#36363e")),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(
                onClick = { onClick(transaction) }
            )
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ){
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = parseTransactionType(transaction.transactiontype),
                    style = TextStyle(
                        color = Color.White,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontFamily = marcherFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                )

                Text(
                    text = "ID: ${transaction.transactionid.substring(0, 8)}...",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = MaterialTheme.typography.titleSmall.fontSize,
                        fontFamily = marcherFontFamily,
                    )
                )
            }

            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column (
                    horizontalAlignment = Alignment.Start,
                ){
                    val date = americanDateToEuropean(transaction.timestamp.split("T")[0])
                    Text(
                        text = "Date : $date",
                        style = TextStyle(
                            color = Color.LightGray,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            fontFamily = marcherFontFamily,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "Total : ${formatPrice(transaction.total)}",
                        style = TextStyle(
                            color = Color.LightGray,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            fontFamily = marcherFontFamily,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ){
                    if (transaction.vouchersUsed.isNotEmpty()) {
                        val cnt = transaction.vouchersUsed.size
                        Text(
                            text = "$cnt" + (if (cnt != 1) " vouchers" else " voucher") + " used",
                            style = TextStyle(
                                color = Color.LightGray,
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontFamily = marcherFontFamily
                            )
                        )
                    }
                    if (transaction.vouchersGenerated.isNotEmpty()) {
                        val cnt = transaction.vouchersGenerated.size
                        Text(
                            text = "$cnt" + (if (cnt != 1) " vouchers" else " voucher") + " generated",
                            style = TextStyle(
                                color = Color.LightGray,
                                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                fontFamily = marcherFontFamily
                            )
                        )
                    }
                }
            }
            // more details
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp)
            ){
                Text(
                    text = "Click for more details",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        fontFamily = marcherFontFamily
                    )
                )
            }
        }




            /*Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(0.65f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                }

                Spacer(modifier = Modifier.height(6.dp))

                Spacer(modifier = Modifier.height(16.dp))




                Spacer(modifier = Modifier.height(3.dp))

                if (transaction.vouchersUsed.isNotEmpty()) {
                    val cnt = transaction.vouchersUsed.size
                    Text(
                        text = "You used $cnt " + (if (cnt != 1) " vouchers" else " voucher") + " in this transaction",
                        style = TextStyle(
                            color = androidx.compose.ui.graphics.Color.LightGray,
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            fontFamily = marcherFontFamily
                        )
                    )
                }
            }*/
    }
}

fun parseTransactionType(transactionType: String): String {
    return when (transactionType) {
        "TICKET_PURCHASE" -> "Ticket Purchase"
        "CAFETERIA_ORDER" -> "Cafeteria Order"
        else -> "Unknown"
    }
}

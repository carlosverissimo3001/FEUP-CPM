package org.feup.carlosverissimo3001.theaterpal.screens.fragments.Wallet.Transactions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import org.feup.carlosverissimo3001.theaterpal.R
import org.feup.carlosverissimo3001.theaterpal.marcherFontFamily
import org.feup.carlosverissimo3001.theaterpal.models.CafeteriaTransactionItem
import org.feup.carlosverissimo3001.theaterpal.models.TicketItem
import org.feup.carlosverissimo3001.theaterpal.models.Transaction
import org.feup.carlosverissimo3001.theaterpal.models.parseVoucherType
import org.feup.carlosverissimo3001.theaterpal.screens.fragments.Cafeteria.formatPrice
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs

@Composable
fun TransactionDetails(
    nif: String,
    transaction: Transaction,
    onCancel: () -> Unit,
    isInspecting: Boolean
) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

    var discount = 0.0
    
    val textureImage = painterResource(id = R.drawable.paper_texture)

    AnimatedVisibility (
        visible = isInspecting,
        enter = slideInVertically(
            // Slide in from the bottom
            initialOffsetY = { it },
            animationSpec = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        ),
        exit = slideOutVertically(
            // Slide out to the bottom
            targetOffsetY = { it },
            animationSpec = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        ),
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp)
                .pointerInput(Unit) {
                    // draw down == go back
                    detectDragGestures { change, dragAmount ->
                        if (dragAmount.y > 50) {
                            onCancel()
                        }
                    }
                }
        ){
            /*Image(
                painter = textureImage,
                contentDescription = "Texture",
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )*/

            // NAME OF THEATER
            // ADDRESS OF THEATER
            Column (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "CASA DA MÚSICA",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                        fontFamily = marcherFontFamily,
                    ),
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                )

                Text(
                    text = "Fundaçao Casa da Música SA",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        fontFamily = marcherFontFamily,
                    ),
                    color = Color.Black,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Text(
                    text = "Edifício da Casa da Música (Av. da Boavista, 604-610) 4100-071 Porto",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleSmall.fontSize,
                        fontFamily = marcherFontFamily,
                    ),
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Text(
                    text = "NIF: 507636295",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleSmall.fontSize,
                        fontFamily = marcherFontFamily,
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // the items
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Text(
                    text = parseTransactionType(transaction.transactiontype),
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        fontFamily = marcherFontFamily,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (transaction.transactiontype == "TICKET_PURCHASE"){
                    val items = transaction.items.map { it as TicketItem }
                    TicketItems(items)
                } else {
                    val items = transaction.items.map { it as CafeteriaTransactionItem }

                    discount = abs(transaction.total - items.sumOf { it.price * it.quantity })

                    CafeteriaItems(items)
                }
            }

            Column {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        text = "TOTAL",
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            fontFamily = marcherFontFamily,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.Black,
                    )

                    Text(
                        text = formatPrice(transaction.total),
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            fontFamily = marcherFontFamily,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.Black,
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        text = "Credit Card (ending in xxxx3919)",
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.titleSmall.fontSize,
                            fontFamily = marcherFontFamily,
                        ),
                        color = Color.Black,
                    )

                    Text(
                        text = formatPrice(transaction.total),
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.titleSmall.fontSize,
                            fontFamily = marcherFontFamily,
                        ),
                        color = Color.Black,
                    )
                }

                // if used a voucher of type 5% discount
                if (transaction.vouchersUsed.any { parseVoucherType(it.voucherType) == "5% Discount" }){
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Discount (5%)",
                            style = TextStyle(
                                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                fontFamily = marcherFontFamily,
                            ),
                            color = Color.Black,
                        )

                        Text(
                            text = formatPrice(discount),
                            style = TextStyle(
                                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                fontFamily = marcherFontFamily,
                            ),
                            color = Color.Black,
                        )
                    }
                }


                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 1.dp,
                    color = Color.Black
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ){
                    Text(
                        text = "Final Consumer NIF: $nif",
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.titleSmall.fontSize,
                            fontFamily = marcherFontFamily,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.Black,
                    )
                    Text(
                        // use the current date and time
                        text = "Transaction Date: ${LocalDateTime.parse(transaction.timestamp).format(formatter)}",
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.titleSmall.fontSize,
                            fontFamily = marcherFontFamily,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.Black,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Vouchers Used
                if (transaction.vouchersUsed.isNotEmpty()){
                    Text(
                        text = "Vouchers Used",
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            fontFamily = marcherFontFamily,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.Black,
                        modifier = Modifier.padding(top = 16.dp)
                    )

                    transaction.vouchersUsed.forEach{voucher ->
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ){
                            Text(
                                text = parseVoucherType(voucher.voucherType),
                                style = TextStyle(
                                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                    fontFamily = marcherFontFamily,
                                ),
                                color = Color.Black,
                            )
                            Text(
                                text = "ID: ${voucher.voucherid.substring(0, 8)}...",
                                style = TextStyle(
                                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                    fontFamily = marcherFontFamily,
                                ),
                                color = Color.Black,
                            )

                        }
                    }
                }

                // Vouchers Earned
                if (transaction.vouchersGenerated.isNotEmpty()){
                    Text(
                        text = "Vouchers Earned",
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            fontFamily = marcherFontFamily,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.Black,
                        modifier = Modifier.padding(top = 32.dp)
                    )
                    var i = 0
                    transaction.vouchersGenerated.forEach{voucher ->
                        i += 1
                        if (i == 5){
                            Text(
                                text = "And ${transaction.vouchersGenerated.size - 4} more...",
                                style = TextStyle(
                                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                    fontFamily = marcherFontFamily,
                                ),
                                color = Color.Black,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                            return@forEach
                        }
                        if (i > 5) return@forEach

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ){
                            Text(
                                text = parseVoucherType(voucher.voucherType),
                                style = TextStyle(
                                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                    fontFamily = marcherFontFamily,
                                ),
                                color = Color.Black,
                            )
                            Text(
                                text = "ID: ${voucher.voucherid.substring(0, 8)}...",
                                style = TextStyle(
                                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                    fontFamily = marcherFontFamily,
                                ),
                                color = Color.Black,
                            )
                        }
                    }
                }
            }



            Button(
                onClick = { onCancel() },
                modifier = Modifier
                    .fillMaxWidth(0.6f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            ) {
                Text(
                    text = "< Take me back",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontFamily = marcherFontFamily,
                    ),
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
            }

        }
    }
}

@Composable
fun TicketItems(items: List<TicketItem>){
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ){
        items.forEach {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ){
                    Text(
                        text = it.showname,
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            fontFamily = marcherFontFamily,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.Black,
                    )
                    Text(
                        text = "Date: ${americanDateToEuropean(it.date)}",
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            fontFamily = marcherFontFamily,
                        ),
                        color = Color.Black,
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ){
                    Text(
                        text = "${it.numtickets} x ",
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            fontFamily = marcherFontFamily,
                        ),
                        color = Color.Black,
                    )

                    Text(
                        text = formatPrice(it.price),
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.titleSmall.fontSize,
                            fontFamily = marcherFontFamily,
                        ),
                        color = Color.Black,
                    )

                    Text(
                        text = formatPrice(it.price * it.numtickets),
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            fontFamily = marcherFontFamily,
                        ),
                        color = Color.Black,
                    )
                }
            }

        }
    }

}

@Composable
fun CafeteriaItems(items: List<CafeteriaTransactionItem>){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(8.dp)
            )
    ){
        items.forEach {
            val isFree = it.price == 0.0

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 1.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row (
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth(0.5f)
                    ){
                        Text(
                            text = it.itemname,
                            style = TextStyle(
                                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                fontFamily = marcherFontFamily,
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.Black,
                        )
                    }

                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            text = formatPrice(it.price),
                            style = TextStyle(
                                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                fontFamily = marcherFontFamily,
                                textDecoration = if (isFree) TextDecoration.LineThrough else null
                            ),
                            color = Color.Black,
                        )

                        Text(
                            text = "x ${it.quantity}",
                            style = TextStyle(
                                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                fontFamily = marcherFontFamily,
                            ),
                            color = Color.Black,
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(0.5f),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = formatPrice(it.price * it.quantity),
                                style = TextStyle(
                                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                    fontFamily = marcherFontFamily,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.Black,
                            )
                        }
                    }
                }
            }
        }
    }
}

fun americanDateToEuropean(date: String): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    val date = formatter.parse(date)
    val formatter2 = SimpleDateFormat("dd/MM/yyyy")
    return formatter2.format(date)
}
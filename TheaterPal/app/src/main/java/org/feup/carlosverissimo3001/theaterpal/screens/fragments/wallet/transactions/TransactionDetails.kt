package org.feup.carlosverissimo3001.theaterpal.screens.fragments.wallet.transactions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import org.feup.carlosverissimo3001.theaterpal.R
import org.feup.carlosverissimo3001.theaterpal.formatPrice
import org.feup.carlosverissimo3001.theaterpal.marcherFontFamily
import org.feup.carlosverissimo3001.theaterpal.models.Parser.parseVoucherType
import org.feup.carlosverissimo3001.theaterpal.models.transaction.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetails(
    nif: String,
    name: String = "Your Name",
    transaction: Transaction,
    onCancel: () -> Unit,
    isInspecting: Boolean
) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

    var discount = 0.0
    
    val textureImage = painterResource(id = R.drawable.paper_texture)
    val sheetState = rememberModalBottomSheetState()

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
        ModalBottomSheet(
            onDismissRequest = { onCancel() },
            sheetState = sheetState,
            containerColor = Color(0xFF1F1F1F),
        ) {
            Column {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Image(
                        painter = textureImage,
                        contentDescription = "Texture",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.matchParentSize(),
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        // NAME OF THEATER
                        // ADDRESS OF THEATER
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
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

                            if (transaction.transactiontype == "TICKET_PURCHASE") {
                                val items = transaction.items.map { it as TransactionTicketItem }
                                TicketItems(items)
                            } else {
                                val items = transaction.items.map { it as TransactionCafeteriaItem }

                                discount =
                                    abs(transaction.total - items.sumOf { it.price * it.quantity })

                                CafeteriaItems(items)
                            }
                        }

                        Column {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
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
                            if (transaction.vouchersUsed.any { parseVoucherType(it.voucherType) == "5% Discount" }) {
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
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Name: $name",
                                    style = TextStyle(
                                        fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                        fontFamily = marcherFontFamily,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Color.Black,
                                )
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
                                    text = "Transaction Date: ${
                                        LocalDateTime.parse(transaction.timestamp).format(formatter)
                                    }",
                                    style = TextStyle(
                                        fontSize = MaterialTheme.typography.titleSmall.fontSize,
                                        fontFamily = marcherFontFamily,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Color.Black
                                )
                                Text(
                                    // use the current date and time
                                    text = "Transaction ID: ${
                                        transaction.transactionid
                                    }",
                                    style = TextStyle(
                                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                        fontFamily = marcherFontFamily,
                                        fontWeight = FontWeight.Normal
                                    ),
                                    color = Color.Black,
                                    textAlign = TextAlign.Center
                                )
                            }

                            // Vouchers Used
                            if (transaction.vouchersUsed.isNotEmpty()) {
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

                                transaction.vouchersUsed.forEach { voucher ->
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                    ) {
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
                            if (transaction.vouchersGenerated.isNotEmpty()) {
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
                                transaction.vouchersGenerated.forEach { voucher ->
                                    i += 1
                                    if (i == 5) {
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
                                    ) {
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
                        
                            // Thank you blah blah
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp)
                            ) {
                                Text(
                                    text = "Thank you for your purchase!",
                                    style = TextStyle(
                                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                        fontFamily = marcherFontFamily,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Color.Black,
                                    modifier = Modifier.padding(top = 32.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TicketItems(items: List<TransactionTicketItem>){
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
                        modifier = Modifier.fillMaxWidth(0.5f),
                        textAlign = TextAlign.Center
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
fun CafeteriaItems(items: List<TransactionCafeteriaItem>){
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

/**
 * Converts an American date format (yyyy-MM-dd) to a European date format (dd/MM/yyyy).
 *
 * @param americanDate The date string in the American date format (yyyy-MM-dd).
 * @return The date string in the European date format (dd/MM/yyyy).
 */
fun americanDateToEuropean(americanDate: String): String {
    // Define the date format for parsing the American date with Locale.US
    val americanDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    // Parse the American date string to a Date object
    val parsedDate = americanDateFormat.parse(americanDate)

    // Check if the parsing was successful
    return if (parsedDate != null) {
        // Define the date format for formatting the European date
        val europeanDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        // Format the parsed date to the European date format
        europeanDateFormat.format(parsedDate)
    } else {
        // If parsing fails, return the original date string
        americanDate
    }
}
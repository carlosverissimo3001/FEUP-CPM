package org.feup.carlosverissimo3001.theaterbite.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import kotlinx.coroutines.*
import org.feup.carlosverissimo3001.theaterbite.formatPrice
import org.feup.carlosverissimo3001.theaterbite.itemIcons
import org.feup.carlosverissimo3001.theaterbite.models.*
import org.feup.carlosverissimo3001.theaterbite.parseVoucherType
import org.feup.carlosverissimo3001.theaterbite.poppinsFontFamily

@Composable
fun OrderConfirmationScreen(
    order: ConfirmedOrder,
    user: User,
    seconds: Int
) {
    var secondsRemaining by remember { mutableIntStateOf(seconds) }
    var job by remember { mutableStateOf<Job?>(null) }

    LaunchedEffect(secondsRemaining) {
        job?.cancel()
        job = CoroutineScope(Dispatchers.Default).launch {
            repeat(secondsRemaining) {
                delay(1000)
                withContext(Dispatchers.Main) {
                    secondsRemaining--
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xFF1F1F1F),
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OrderConfirmationTopBar()

            // ORDER NUMBER
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            )
            {
                Text(
                    text = "Your Order Number is ",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                        fontFamily = poppinsFontFamily,
                    ),
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = order.orderNo.toString(),
                    style = TextStyle(
                        color = Color.White,
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center,
                )
            }

            Text(
                text = "Please check your order number on the screen and wait for it to be ready.",
                modifier = Modifier.padding(
                    start = 40.dp,
                    end = 40.dp,
                    top = 10.dp,
                    bottom = 20.dp
                ),
                style = TextStyle(
                    color = Color(0x77FFFFFF),
                    fontSize = 14.sp,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Normal
                ),
                textAlign = TextAlign.Center
            )

            // ORDER DETAILS

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 20.dp),
                color = Color.White
            )

            OrderDetails(
                products = order.products
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 20.dp),
                color = Color.White
            )

            if (order.vouchersUsed.isNotEmpty()) {
                Vouchers(
                    vouchers = order.vouchersUsed
                )
            }

            if (order.vouchersGenerated.isNotEmpty()) {
                Vouchers(
                    vouchers = order.vouchersUsed,
                    generated = true
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 20.dp),
                color = Color.White
            )

            // ORDER TOTAL
            Row(
                modifier = Modifier.padding(top = 20.dp, bottom = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Total: ",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                        fontFamily = poppinsFontFamily,
                    ),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = formatPrice(order.total),
                    style = TextStyle(
                        color = Color.White,
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )
            }

            Text(
                text = "(The amount has already been deducted from your card)",
                style = TextStyle(
                    color = Color.White,
                    fontSize = MaterialTheme.typography.labelMedium.fontSize,
                    fontFamily = poppinsFontFamily,
                ),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Thank you for your purchase!",
                style = TextStyle(
                    color = Color.White,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 20.dp)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(bottom = 25.dp, start = 20.dp, end = 20.dp).align(Alignment.BottomCenter)
        ){
            Text(
                text = "Returning to the main screen in ",
                style = TextStyle(
                    color = Color.White,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontFamily = poppinsFontFamily,
                ),
                textAlign = TextAlign.Center,
            )
            Text(
                text = "$secondsRemaining seconds",
                style = TextStyle(
                    color = Color.White,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center,
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderConfirmationTopBar() {
    CenterAlignedTopAppBar(
        modifier = Modifier.padding(top = 20.dp),
        title = {
            Text(
                "Order Confirmed!",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 33.sp,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )
        },

        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
        ),
    )
}

/**
 * OrderDetails composable that displays the products in the order
 * @param products List of products in the order
 */
@Composable
fun OrderDetails(products: List<Product>) {
    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Order Details:",
            style = TextStyle(
                color = Color.White,
                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Bold
            ),
        )

        products.forEach {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = itemIcons[it.name]!!,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.padding(end = 10.dp).size(25.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${it.quantity}x ${it.name}",/* - ",*/
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 16.sp,
                            fontFamily = poppinsFontFamily,
                        ),
                    )
                }
            }
        }
    }
}

/**
 * Composable that displays the vouchers used in the order
 * @param vouchers List of vouchers of the order (by default used)
 * @param generated If true, the list is that of the generated vouchers
 */
@Composable
fun Vouchers(vouchers: List<Voucher>, generated: Boolean = false){
    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Vouchers" + if (generated) " Generated:" else " Used:",
            style = TextStyle(
                color = Color.White,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFontFamily,
            ),
            textAlign = TextAlign.Center
        )
        vouchers.forEach{
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "ID: ${it.voucherid.substring(0, 8)}...",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 14.sp,
                        fontFamily = poppinsFontFamily,
                    ),
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "Type: ",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 14.sp,
                        fontFamily = poppinsFontFamily,
                    ),
                )

                Text(
                    text = parseVoucherType(it.voucherType),
                    style = TextStyle(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        fontFamily = poppinsFontFamily,
                    ),
                )
            }
        }
    }
}
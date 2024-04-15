package org.feup.carlosverissimo3001.theaterbite

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.substring
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.*
import org.feup.carlosverissimo3001.theaterbite.models.ConfirmedOrder
import org.feup.carlosverissimo3001.theaterbite.models.Order
import org.feup.carlosverissimo3001.theaterbite.models.parseVoucherType

class OrderConfirmationActivity : AppCompatActivity() {
    private val delayMillis = 10000L // 10 seconds
    private lateinit var job: Job
    private lateinit var order: ConfirmedOrder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        order = intent.parcelable("order")!!

        job = CoroutineScope(Dispatchers.Main).launch {
            delay(delayMillis)
            finish()
        }

        setContent {
            OrderConfirmationScreen(
                order,
                seconds = 10
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel() // Cancel the coroutine to avoid leaks
    }

    @Composable
    fun OrderConfirmationScreen(
        order: ConfirmedOrder,
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

                OrderDetails()

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = Color.White
                )

                if (order.vouchersUsed.isNotEmpty()) {
                    VouchersUsed()
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

    @Composable
    fun VouchersUsed() {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Vouchers Accepted:",
                style = TextStyle(
                    color = Color.White,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFontFamily,
                ),
                textAlign = TextAlign.Center
            )
            order.vouchersUsed.forEach() {
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

    @Composable
    fun OrderDetails() {
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

            order.products.forEach() {
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
                        /*Text(
                            text = "${it.price}€",
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 16.sp,
                                fontFamily = poppinsFontFamily,
                            ),
                            textDecoration = if (it.name.startsWith("Free")) TextDecoration.LineThrough else TextDecoration.None
                        )*/
                    }
                }
            }
        }
    }

}






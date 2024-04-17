package org.feup.carlosverissimo3001.theaterpal.screens.fragments.cafeteria.ordering

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.dp
import org.feup.carlosverissimo3001.theaterpal.R
import org.feup.carlosverissimo3001.theaterpal.formatPrice
import org.feup.carlosverissimo3001.theaterpal.marcherFontFamily
import org.feup.carlosverissimo3001.theaterpal.models.Parser.parseVoucherType
import org.feup.carlosverissimo3001.theaterpal.models.order.Order

@Composable
fun SendingOrderFragment(
    isSending: Boolean,
    onCancel: () -> Unit = {},
    order: Order? = null
) {
    AnimatedVisibility (
        visible = isSending,
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
    ) {
        Box(
            modifier = Modifier
                .background(
                    Color(android.graphics.Color.parseColor("#302c2c")),
                    RoundedCornerShape(15.dp)
                )
                .fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize().padding(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(6.dp)
                        .background(Color.White, CircleShape)
                        .pointerInput(Unit) {
                            detectDragGestures { _, dragAmount ->
                                if (dragAmount.y > 0) {
                                    // Dragging down, cancel the scanning
                                    onCancel()
                                }
                            }
                        }
                )

                Column (
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .padding(12.dp)
                        .fillMaxWidth(0.9f)

                ){
                    Text(
                        text = "Your Order: ",
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                            fontFamily = marcherFontFamily,
                            fontWeight = FontWeight.Bold,
                        ),
                        color = Color.White,
                    )

                    Spacer(modifier = Modifier.size(10.dp))

                    order?.barOrder?.items?.forEach {
                        Text(
                            text = "· ${it.key.name} x ${it.value}",
                            style = TextStyle(
                                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                fontFamily = marcherFontFamily,
                            ),
                            color = Color.White,
                        )
                    }


                    if (order?.vouchersUsed?.isNotEmpty() == true){
                        Spacer(modifier = Modifier.size(25.dp))

                        Text(
                                text = "You choose the following voucher(s): ",
                                style = TextStyle(
                                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                    fontFamily = marcherFontFamily,
                                    fontWeight = FontWeight.Bold,
                                ),
                                color = Color.White,
                                textAlign = TextAlign.Center,
                            )

                        Spacer(modifier = Modifier.size(10.dp))
                    }

                    order?.vouchersUsed?.forEach {
                        Row (
                            verticalAlignment = Alignment.CenterVertically,
                        ){
                            Text(
                                text = "· ${parseVoucherType(it.voucherType)}",
                                style = TextStyle(
                                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                    fontFamily = marcherFontFamily,
                                ),
                                color = Color.White,
                            )
                            Text(
                                text = "\t (ID: ${it.voucherid.substring(0, 8)}...)",
                                style = TextStyle(
                                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                    fontFamily = marcherFontFamily,
                                ),
                                color = Color.White,
                            )
                        }
                    }

                    Spacer(modifier = Modifier.size(25.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Your total is: ",
                            style = TextStyle(
                                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                fontFamily = marcherFontFamily
                            ),
                            color = Color.White,
                        )

                        Text(
                            text = formatPrice(order?.barOrder?.total ?: 0.0),
                            style = TextStyle(
                                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                fontFamily = marcherFontFamily,
                                fontWeight = FontWeight.Bold,
                            ),
                            color = Color.White,
                        )
                    }
                }


                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Text(
                        text = "Please head to the cafeteria terminal and tap your device",
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            fontFamily = marcherFontFamily,
                        ),
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                    )
                    
                    Spacer(modifier = Modifier.size(10.dp))

                    Text(
                        text = "(Make sure NFC is active on your device)",
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.titleSmall.fontSize,
                            fontFamily = marcherFontFamily,
                        ),
                        color = Color.White,
                    )
                    
                    Spacer(modifier = Modifier.size(20.dp))
                    
                    Image(
                        painter = painterResource(id = R.drawable.nfc_scanning),
                        contentDescription = "NFC action",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxHeight(0.5f)
                    )
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
}
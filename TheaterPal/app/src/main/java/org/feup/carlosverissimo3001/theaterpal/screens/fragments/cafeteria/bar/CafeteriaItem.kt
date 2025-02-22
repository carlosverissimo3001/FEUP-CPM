package org.feup.carlosverissimo3001.theaterpal.screens.fragments.cafeteria.bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import org.feup.carlosverissimo3001.theaterpal.formatPrice
import org.feup.carlosverissimo3001.theaterpal.marcherFontFamily
import org.feup.carlosverissimo3001.theaterpal.models.Auxiliary.itemEmojis
import org.feup.carlosverissimo3001.theaterpal.models.order.CafeteriaItem

@Composable
fun CafeteriaItem(
    item: CafeteriaItem,
    onIncrement: (CafeteriaItem) -> Unit,
    onDecrement: (CafeteriaItem) -> Unit
) {
    val (quantity, setQuantity) = remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .size(120.dp)
            .padding(10.dp)
            .background(
                color = Color(android.graphics.Color.parseColor("#36363e")),
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        // Icon, Name and Description
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
        ) {
            Text(
                text = itemEmojis[item.name]!!,
                style = TextStyle(
                    fontFamily = marcherFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.size(30.dp).padding(end = 8.dp)
            )

            // Name and Description
            Column (
                modifier = Modifier.size(140.dp, 120.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = item.name,
                    style = TextStyle(
                        fontFamily = marcherFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize
                    )
                )
                Text(
                    text = item.description,
                    style = TextStyle(
                        fontFamily = marcherFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = MaterialTheme.typography.titleSmall.fontSize
                    )
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Price
                Text(
                    text = formatPrice(item.price),
                    style = TextStyle(
                        fontFamily = marcherFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize
                    )
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Decrement button
                    TextButton(
                        onClick = {
                            if (quantity > 0) {
                                setQuantity(quantity - 1)
                                onDecrement(item)
                            } },
                        modifier = Modifier.background(
                            color = Color(android.graphics.Color.parseColor("#36363e")),
                            shape = RoundedCornerShape(16.dp)
                        )
                    ) {
                        Text(
                            text = "-",
                            style = TextStyle(
                                fontFamily = marcherFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        )
                    }

                    // Quantity
                    Text(
                        text = quantity.toString(),
                        style = TextStyle(
                            fontFamily = marcherFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    )

                    // Increment button
                    TextButton(
                        onClick = {
                            setQuantity(quantity + 1)
                            onIncrement(item)
                        }

                    ) {
                        Text(
                            text = "+",
                            style = TextStyle(
                                fontFamily = marcherFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        )
                    }
                }
            }
        }
    }
}


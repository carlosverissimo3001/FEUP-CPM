package org.feup.carlosverissimo3001.theaterpal.screens.fragments.cafeteria.bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.feup.carlosverissimo3001.theaterpal.formatPrice
import org.feup.carlosverissimo3001.theaterpal.marcherFontFamily
import org.feup.carlosverissimo3001.theaterpal.models.Auxiliary.getCafeteriaItems
import org.feup.carlosverissimo3001.theaterpal.models.order.*

@Composable
fun BarTab(onNextStepClick: (BarOrder) -> Unit){
    val barItems : List<CafeteriaItem> = getCafeteriaItems()
    var total by remember { mutableDoubleStateOf(0.0) }

    var order by remember { mutableStateOf(emptyMap<CafeteriaItem, Int>()) }

    Box (
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            contentPadding = PaddingValues(10.dp)
        ) {
            items(barItems.size) { index ->
                CafeteriaItem(
                    item = barItems[index],
                    onIncrement = { item ->
                        total += item.price
                        order = order.toMutableMap().apply {
                            this[item] = (this[item] ?: 0) + 1
                        }
                    },
                    onDecrement = { item ->
                        total -= item.price
                        order = order.toMutableMap().apply {
                            this[item] = (this[item] ?: 0) - 1

                            // remove item from order if quantity is 0
                            if (this[item] == 0) {
                                this.remove(item)
                            }
                        }
                    },
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // TOTAL
            Box (
                modifier = Modifier.size(200.dp, 50.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = Color(android.graphics.Color.parseColor("#36363e")),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .size(120.dp, 40.dp)
                        .padding(start = 10.dp, end = 10.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row (
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total: ",
                            style = TextStyle(
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontFamily = marcherFontFamily,
                                fontSize = MaterialTheme.typography.titleMedium.fontSize
                            )
                        )
                        Text(
                            text = formatPrice(total),
                            style = TextStyle(
                                color = Color.White,
                                fontWeight = FontWeight.Normal,
                                fontFamily = marcherFontFamily,
                                fontSize = MaterialTheme.typography.titleMedium.fontSize
                            )
                        )
                    }
                }
            }

            // NEXT STEP
            Button(
                enabled = total > 0,
                onClick = {
                    onNextStepClick(BarOrder(items = order, total = total))
                },
                modifier = Modifier
                    .width(200.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF43A047),
                    contentColor = Color.White
                ),
            ) {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Next Step >",
                        style = TextStyle(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontFamily = marcherFontFamily,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize
                        )
                    )
                    Text(
                        text = "Apply vouchers",
                        style = TextStyle(
                            color = Color.White,
                            fontWeight = FontWeight.Normal,
                            fontFamily = marcherFontFamily,
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize
                        )
                    )
                }
            }
        }
    }
}

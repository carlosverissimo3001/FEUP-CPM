package org.feup.carlosverissimo3001.theaterpal.screens.fragments.Cafeteria

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.feup.carlosverissimo3001.theaterpal.marcherFontFamily
import org.feup.carlosverissimo3001.theaterpal.models.BarOrder
import org.feup.carlosverissimo3001.theaterpal.models.Order
import org.feup.carlosverissimo3001.theaterpal.models.Voucher
import org.feup.carlosverissimo3001.theaterpal.models.parseVoucherType
import kotlin.math.round

@Composable
fun VouchersTab(
    ctx: Context,
    vouchers: List<Voucher>,
    onFilterChanged: (Boolean) -> Unit,
    isChoosingVoucher: Boolean,
    onSubmitted: (List<Voucher>) -> Unit,
    total : Double = 0.0
) {
    val (isChecked, setChecked) = remember { mutableStateOf(true) }
    val selectedVouchers = remember { mutableStateOf(emptyList<Voucher>()) }
    var updatedTotal by remember { mutableDoubleStateOf(total) }

    val possibleDiscount = String.format("%.2f", total*0.05).toDouble()


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.size(16.dp))

        if (!isChoosingVoucher) {
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = {
                        setChecked(it)
                        onFilterChanged(it)
                    },
                )
                Text(text = "View only active vouchers", style = TextStyle(
                    fontFamily = marcherFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                ))
            }
        }
        else {
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Please select up to 2 vouchers", style = TextStyle(
                    fontFamily = marcherFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                ))
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            contentPadding = PaddingValues(10.dp)
        ) {
            items(vouchers.size) { index ->
                Voucher(
                    voucher = vouchers[index],
                    selectionMode = isChoosingVoucher,
                    onSelected = { voucher ->
                        if (selectedVouchers.value.contains(voucher)) {
                            selectedVouchers.value = selectedVouchers.value.filter { it != voucher }

                            if (parseVoucherType(voucher.voucherType) == "5% Discount") {
                                updatedTotal += possibleDiscount
                            }

                        } else {
                            if (selectedVouchers.value.size < 2) {
                                selectedVouchers.value += voucher
                            }
                            if (parseVoucherType(voucher.voucherType) == "5% Discount") {
                                updatedTotal -= possibleDiscount
                            }
                        }
                    }
                )
            }
        }

        if (isChoosingVoucher) {
            FloatingActionButton(
                onClick = {
                    onSubmitted(selectedVouchers.value)
                },
                modifier = Modifier
                    .width(200.dp),
                containerColor = Color(0xFF43A047),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Submit Order",
                        style = TextStyle(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontFamily = marcherFontFamily,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize
                        )
                    )
                    Text(
                        text = "Your total is ${formatPrice(updatedTotal)}",
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


package org.feup.carlosverissimo3001.theaterpal.screens.fragments.cafeteria.voucher

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color as ComposeColor
import android.graphics.Color.parseColor
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.feup.carlosverissimo3001.theaterpal.ParseIsUsed
import org.feup.carlosverissimo3001.theaterpal.marcherFontFamily
import org.feup.carlosverissimo3001.theaterpal.models.Parser.parseVoucherType
import org.feup.carlosverissimo3001.theaterpal.models.Voucher

@Composable
fun Voucher(
    voucher: Voucher,
    canSelect: Boolean = true,
    selectionMode : Boolean = false,
    onSelected: (Voucher, Boolean) -> Unit = { _, _ -> },
    isDiscountAlreadyApplied: Boolean = false
) {

    val (isSelected, setSelected) = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(10.dp)
            .clickable(
                onClick = {
                    if (isDiscountAlreadyApplied && parseVoucherType(voucher.voucherType) == "5% Discount") {
                        return@clickable
                    }

                    if (selectionMode) {
                        if (canSelect) {
                            setSelected(!isSelected)
                            onSelected(voucher, true)
                        } else {
                            onSelected(voucher, false)
                        }
                    }
                }
            )
            .background(
                color = ComposeColor(parseColor("#36363e")),
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 2.dp,
                color = if (isSelected) ComposeColor(parseColor("#FF43A047")) else ComposeColor.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(12.dp)
        ) {
            Column {
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ){
                    Text(
                        text = parseVoucherType(voucher.voucherType),
                        style = TextStyle(
                            color = ComposeColor.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            fontFamily = marcherFontFamily
                        ),
                    )
                    Text(
                        text = getEmoji(parseVoucherType(voucher.voucherType)),
                        style = TextStyle(
                            color = ComposeColor.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            fontFamily = marcherFontFamily
                        ),
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                ParseIsUsed(voucher.isUsed)

                Spacer(modifier = Modifier.height(3.dp))
            }
            Text(
                text = "Unique ID: ${voucher.voucherid.substring(0, 8)}...",
                style = TextStyle(
                    color = ComposeColor.White,
                    fontWeight = FontWeight.Normal,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    fontFamily = marcherFontFamily
                ),
            )
        }
    }
}

fun getEmoji(type: String) : String {
    return when (type) {
        "5% Discount" -> " \uD83C\uDFF7\uFE0F"
        "Free Popcorn" -> " 🍿"
        "Free Coffee" -> " ☕"
        else -> "🎟️"
    }
}


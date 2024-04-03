package org.feup.carlosverissimo3001.theaterpal.screens.fragments.Cafeteria

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.feup.carlosverissimo3001.theaterpal.marcherFontFamily
import org.feup.carlosverissimo3001.theaterpal.models.Voucher
import org.feup.carlosverissimo3001.theaterpal.models.parseVoucherType

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

                    if (selectionMode){
                        if (canSelect){
                            setSelected(!isSelected)
                            onSelected(voucher, true)
                        }
                        else{
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(
                        text = parseVoucherType(voucher.voucherType),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            color = ComposeColor.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            fontFamily = marcherFontFamily
                        ),
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                parseIsUsed(voucher.isUsed)

                Spacer(modifier = Modifier.height(3.dp))
            }
        }
    }
}


@Composable
fun parseIsUsed (isUsed: Boolean) {
    return if (isUsed)
        Text (
            text = "Used",
            style = TextStyle(
                color = ComposeColor.Red,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                fontFamily = marcherFontFamily
            )
        )
    else
        Text (
            text = "Active",
            style = TextStyle(
                color = ComposeColor.Green,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                fontFamily = marcherFontFamily
            )
        )
}

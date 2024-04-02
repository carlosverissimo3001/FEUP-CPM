package org.feup.carlosverissimo3001.theaterpal.screens.fragments.Cafeteria

import android.graphics.Bitmap
import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.feup.carlosverissimo3001.theaterpal.marcherFontFamily
import org.feup.carlosverissimo3001.theaterpal.models.Voucher

@Composable
fun Voucher(voucher: Voucher) {
    Box(
        modifier = Modifier
            .padding(10.dp)
            .background(
                color = Color(android.graphics.Color.parseColor("#36363e")),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(
                onClick = { println("Clicked on voucher") }
            )
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(0.65f)
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    /*Icon(

                    )*/

                    Text(
                        text = parseType(voucher.voucherType),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            color = androidx.compose.ui.graphics.Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            fontFamily = marcherFontFamily
                        )
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Casa da Música",
                    style = TextStyle(
                        color = androidx.compose.ui.graphics.Color.LightGray,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        fontFamily = marcherFontFamily
                    )
                )

                Spacer(modifier = Modifier.height(3.dp))
            }
        }
    }
}

fun parseType (type: String) : String {
    if (type == "FREE_COFFEE")
        return "Coffee"
    else if (type == "FREE_POPCORN")
        return "Popcorn"
    else
        return "5% Discount"
}

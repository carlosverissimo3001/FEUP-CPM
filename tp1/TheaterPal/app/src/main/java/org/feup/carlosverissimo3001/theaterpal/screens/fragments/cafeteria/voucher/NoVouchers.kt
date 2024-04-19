package org.feup.carlosverissimo3001.theaterpal.screens.fragments.cafeteria.voucher

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.feup.carlosverissimo3001.theaterpal.marcherFontFamily

@Composable
fun NoVouchers(
    onClick: (String) -> Unit,
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No vouchers available :( ",
            style = TextStyle(
                fontFamily = marcherFontFamily,
                color = Color.White,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.padding(16.dp))

        Text(
            text = "You can earn vouchers by:",
            style = TextStyle(
                fontFamily = marcherFontFamily,
                color = Color.White,
                fontSize = MaterialTheme.typography.titleMedium.fontSize
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.padding(12.dp))

        Text(
            text = "\uD83C\uDF9F\uFE0F Buying tickets to shows",
            style = TextStyle(
                fontFamily = marcherFontFamily,
                color = Color.White,
                fontSize = 15.sp,
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.padding(4.dp))

        Button(
            modifier = Modifier.fillMaxWidth(0.7f),
            onClick = {
                onClick("shows")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            )
        ) {
            Text(
                text = "\uD83D\uDC49 Buy tickets",
                style = TextStyle(
                    fontFamily = marcherFontFamily,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(modifier = Modifier.padding(7.dp))

        Text(
            text = "or",
            style = TextStyle(
                fontFamily = marcherFontFamily,
                color = Color.White,
                fontSize = 15.sp,
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.padding(7.dp))

        Text(
            text = "☕ Spending money in the cafeteria",
            style = TextStyle(
                fontFamily = marcherFontFamily,
                color = Color.White,
                fontSize = 15.sp,
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.padding(4.dp))

        Button(
            modifier = Modifier.fillMaxWidth(0.7f),
            onClick = {
                onClick("cafeteria")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            )
        ) {
            Text(
                text = "\uD83D\uDC49 Buy food",
                style = TextStyle(
                    fontFamily = marcherFontFamily,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}
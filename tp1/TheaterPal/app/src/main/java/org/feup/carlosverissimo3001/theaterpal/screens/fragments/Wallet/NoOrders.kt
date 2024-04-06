package org.feup.carlosverissimo3001.theaterpal.screens.fragments.Wallet

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
import org.feup.carlosverissimo3001.theaterpal.marcherFontFamily

@Composable
fun NoOrders(
    onClick: () -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "No orders available :( ",
            style = TextStyle(
                fontFamily = marcherFontFamily,
                color = Color.White,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.padding(16.dp))

        Text(
            text = "To see which products are available, go to the Cafeteria tab",
            style = TextStyle(
                fontFamily = marcherFontFamily,
                color = Color.White,
                fontSize = MaterialTheme.typography.titleMedium.fontSize
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.padding(50.dp))

        Button(
            modifier = Modifier.fillMaxWidth(0.7f),
            onClick = {
                onClick()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            )
        ) {
            Text(
                text = "Buy products",
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
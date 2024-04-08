package org.feup.carlosverissimo3001.theaterpal.screens.fragments.Wallet.Transactions

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
fun NoTransactions(
    onClick: (String) -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "You haven't made any transactions yet :( ",
            style = TextStyle(
                fontFamily = marcherFontFamily,
                color = Color.White,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.padding(16.dp))

        Text(
            text = "You can start by buying products in the Cafeteria tab",
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
                onClick("cafeteria")
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

        Text(
            text = "... or by buying tickets in the Shows tab",
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
                onClick("shows")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            )
        ) {
            Text(
                text = "Buy Tickets",
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
package org.feup.carlosverissimo3001.theaterpal.screens.fragments.Wallet.Orders

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import org.feup.carlosverissimo3001.theaterpal.api.getUserOrders
import org.feup.carlosverissimo3001.theaterpal.auth.Authentication
import org.feup.carlosverissimo3001.theaterpal.marcherFontFamily
import org.feup.carlosverissimo3001.theaterpal.models.OrderRcv
import org.feup.carlosverissimo3001.theaterpal.screens.LoadingSpinner

@Composable
fun OrdersTab(
    ctx: Context,
    orders: List<OrderRcv>,
    onConsultTransactionsClicked: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            contentPadding = PaddingValues(10.dp)
        ) {
            items(orders.size) { index ->
                // Display ticket
                // Use the ticket fragment to display the ticket
                Order(
                    order = orders[index],
                    ctx = ctx
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Click here to consult all transactions",
                style = TextStyle(
                    fontFamily = marcherFontFamily,
                    color = Color.White,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    // change the underline color to white
                    textDecoration = TextDecoration.Underline
                ),
                /*drawUnderline = {

                }*/
                modifier = Modifier.clickable(
                    onClick = {
                        onConsultTransactionsClicked()
                    }
                )
            )
        }
    }
}
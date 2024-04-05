package org.feup.carlosverissimo3001.theaterpal.screens.fragments.Wallet

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.feup.carlosverissimo3001.theaterpal.R
import org.feup.carlosverissimo3001.theaterpal.file.loadImageFromCache
import org.feup.carlosverissimo3001.theaterpal.marcherFontFamily
import org.feup.carlosverissimo3001.theaterpal.models.Ticket

@Composable
fun TicketsTab(
    ctx: Context,
    tickets: List<Ticket>,
    onFilterChanged: (Boolean) -> Unit
) {
    val (isChecked, setFilterChecked) = remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxSize().padding(top = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = {
                    setFilterChecked(it)
                    onFilterChanged(it)
                },
            )
            Text(text = "View only active tickets", style = TextStyle(
                fontFamily = marcherFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.titleMedium.fontSize
            ))
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            contentPadding = PaddingValues(10.dp)
        ) {
            items(tickets.size) { index ->
                // Display ticket
                // Use the ticket fragment to display the ticket
                val imagePath = tickets[index].imagePath
                val bitmap: Bitmap? = loadImageFromCache(imagePath, ctx)
                Ticket(
                    ticket = tickets[index],
                    image = bitmap
                )
            }
        }
    }
}
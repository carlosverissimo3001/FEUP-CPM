package org.feup.carlosverissimo3001.theaterpal.screens.fragments.Wallet.Tickets

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import org.feup.carlosverissimo3001.theaterpal.file.loadImageFromCache
import org.feup.carlosverissimo3001.theaterpal.marcherFontFamily
import org.feup.carlosverissimo3001.theaterpal.models.Ticket

@Composable
fun TicketsTab(
    ctx: Context,
    tickets: List<Ticket>,
    onFilterChanged: (Boolean) -> Unit,
    /*onGroupingChanged: (Boolean) -> Unit,*/
    onValidate: (List<Ticket>) -> Unit,
    onConsultTransactionsClicked: () -> Unit
) {
    val (isChecked, setFilterChecked) = remember { mutableStateOf(true) }
    val (isGrouped, setGroupingChecked) = remember { mutableStateOf(true) }

    var selectedTickets by remember { mutableStateOf(emptyList<Ticket>()) }

    // when a ticket is not in view, its state should still be remembered
    val selectedTicketIndices = remember { mutableStateListOf<Int>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row (
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = {
                        setFilterChecked(it)
                        onFilterChanged(it)
                    },
                )
                Text(
                    text = "View only active tickets", style = TextStyle(
                        fontFamily = marcherFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.titleSmall.fontSize
                    )
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Click here to consult all transactions",
                style = TextStyle(
                    fontFamily = marcherFontFamily,
                    color = Color.White,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    textDecoration = TextDecoration.Underline
                ),
                modifier = Modifier.clickable(
                    onClick = {
                        onConsultTransactionsClicked()
                    }
                )
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            contentPadding = PaddingValues(10.dp)
        ) {
            items(tickets.size) { index ->
                val isSelected = selectedTickets.contains(tickets[index])

                // Display ticket
                // Use the ticket fragment to display the ticket
                val imagePath = tickets[index].imagePath
                val bitmap: Bitmap? = loadImageFromCache(imagePath, ctx)
                Ticket(
                    ticket = tickets[index],
                    image = bitmap,
                    isSelected = isSelected,
                    onClick = {ticket ->
                        if (isSelected) {
                            selectedTickets = selectedTickets.filter { it != ticket }
                            selectedTicketIndices.remove(index)
                        } else if (selectedTickets.size < 4){
                            selectedTickets = selectedTickets + ticket
                            selectedTicketIndices.add(index)
                        }
                    }
                )
            }
        }
    }

    if (selectedTickets.isNotEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            ExtendedFloatingActionButton(
                onClick = {
                    onValidate(selectedTickets)
                },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ) {
                Icon(
                    imageVector = Icons.Filled.VerifiedUser,
                    contentDescription = "Validate Tickets",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Validate ${selectedTickets.size} ticket" + if (selectedTickets.size > 1) "s" else "",
                    style = TextStyle(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontFamily = marcherFontFamily,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize
                    )
                )
            }
        }
    }
}
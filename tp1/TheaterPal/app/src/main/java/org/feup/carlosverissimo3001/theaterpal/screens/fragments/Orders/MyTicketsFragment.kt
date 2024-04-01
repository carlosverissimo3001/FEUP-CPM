package org.feup.carlosverissimo3001.theaterpal.screens.fragments.Orders

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import org.feup.carlosverissimo3001.theaterpal.api.getUserTickets
import org.feup.carlosverissimo3001.theaterpal.auth.Authentication
import org.feup.carlosverissimo3001.theaterpal.file.loadImageFromCache
import org.feup.carlosverissimo3001.theaterpal.models.Ticket
import java.util.Locale

@Composable
fun Orders(ctx: Context) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val ticketsState = remember { mutableStateOf(emptyList<Ticket>()) }
    val loadedTickets = remember { mutableStateOf(false) }
    /*val ordersState = remember { mutableStateOf(emptyList<Ticket>()) }*/

    LaunchedEffect(Unit) {
        getUserTickets(user_id = Authentication(ctx).getUserID()) { tickets ->
            ticketsState.value = tickets
            loadedTickets.value = true
        }
    }

    // Do the same for cafeteria orders

    val ticketArray = ticketsState.value
    val usedTicketArray = ticketArray.filter { it.isUsed }
    var unusedTicketArray = ticketArray.filter { !it.isUsed }
    // unusedTicketArray = groupTickets(unusedTicketArray)

    println("Ticket Array: $ticketArray")

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.LightGray,
            contentColor = Color.Black
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 },
                text = { Text("Tickets") }
            )
            Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
                text = { Text("Cafeteria Orders") }
            )
            /*Tab(
                selected = selectedTabIndex == 2,
                onClick = { selectedTabIndex = 2 },
                text = { Text("Vouchers") }
            )*/
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    // Handle swipe gestures
                    detectDragGestures { change, dragAmount ->
                        if (dragAmount.x > 50) {
                            selectedTabIndex = 0
                        } else if (dragAmount.x < -50) {
                            selectedTabIndex = 1
                        }

                        // TODO: Check for refresh movement
                    }
                }
        ) {
            if (selectedTabIndex == 0) {
                // Hasn't loaded, display loading spinner
                if (!loadedTickets.value) {
                    LoadingSpinner()
                }
                else if (unusedTicketArray.isEmpty()) {
                    // Display no tickets
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("No tickets available", style = MaterialTheme.typography.bodyMedium)
                    }
                }
                else {
                    // Display tickets
                    // Use the ticket fragment to display the tickets
                    Column (
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ){
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(1),
                            contentPadding = PaddingValues(10.dp)
                        ) {
                            items(unusedTicketArray.size) { index ->
                                // Display ticket
                                // Use the ticket fragment to display the ticket
                                val imagePath = unusedTicketArray[index].imagePath
                                val bitmap : Bitmap? = loadImageFromCache(imagePath, ctx)
                                Ticket(
                                    ticket = ticketArray[index],
                                    image = bitmap
                                )
                            }
                        }

                        Button(
                            modifier = Modifier.fillMaxWidth(0.5f),
                            onClick = { /*"Show used tickets"*/ }) {
                            Text("View Used Tickets")
                        }
                    }
                }
            } else if (selectedTabIndex == 1) {
                // Display cafeteria orders tab content
                Icon(
                    imageVector = Icons.Filled.ShoppingCart,
                    contentDescription = "My Cafeteria Orders",
                    tint = Color.Green,
                    modifier = Modifier
                        .size(150.dp)
                        .align(Alignment.Center)
                )
            } else { /*Vouchers Tab*/
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "My Vouchers",
                    tint = Color.Green,
                    modifier = Modifier
                        .size(150.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}


@Composable
fun LoadingSpinner() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            color = Color.White,
            strokeWidth = 2.dp
        )
    }
}

fun groupTickets(tickets: List<Ticket>) : List<Ticket>{
    // If the tickets are for the same show, group them together

    var groupedTickets = mutableListOf<Ticket>()

    for (ticketI in tickets){
        if (groupedTickets.isEmpty()){
            groupedTickets.add(ticketI)
        } else {
            var found = false
            for (ticketJ in groupedTickets){
                if (ticketI.showName == ticketJ.showName){
                    // Add ticketI to the group
                    ticketJ.numTickets += 1
                    found = true
                    break
                }
            }
            if (!found){
                groupedTickets.add(ticketI)
            }
        }
    }

    return groupedTickets
}
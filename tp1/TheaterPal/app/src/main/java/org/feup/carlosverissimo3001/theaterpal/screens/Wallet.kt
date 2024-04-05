package org.feup.carlosverissimo3001.theaterpal.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.feup.carlosverissimo3001.theaterpal.api.getUserTickets
import org.feup.carlosverissimo3001.theaterpal.auth.Authentication
import org.feup.carlosverissimo3001.theaterpal.marcherFontFamily
import org.feup.carlosverissimo3001.theaterpal.models.Ticket
import org.feup.carlosverissimo3001.theaterpal.screens.fragments.Wallet.OrdersTab
import org.feup.carlosverissimo3001.theaterpal.screens.fragments.Wallet.TicketsTab

@Composable
fun Wallet(ctx: Context) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val areTicketsLoaded = remember { mutableStateOf(false) }

    var ticketsState by remember { mutableStateOf(emptyList<Ticket>()) }
    var filteredTickets by remember { mutableStateOf(emptyList<Ticket>()) }

    LaunchedEffect(Unit) {
        getUserTickets(user_id = Authentication(ctx).getUserID()) { tickets ->
            ticketsState = tickets
            areTicketsLoaded.value = true
            filteredTickets = tickets.filter { !it.isUsed}
        }
    }

    // unusedTicketArray = groupTickets(unusedTicketArray)

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary,
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 },
                text = { Text("Tickets",
                    style = TextStyle(
                        fontFamily = marcherFontFamily,
                        color = Color.White,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        fontWeight = (selectedTabIndex == 0).let {
                            if (it) FontWeight.Bold else FontWeight.Normal
                        }
                    )
                )}
            )
            Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
                text = { Text("Cafeteria Orders", style =
                    TextStyle(
                        fontFamily = marcherFontFamily,
                        color = Color.White,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        fontWeight = (selectedTabIndex == 1).let {
                            if (it) FontWeight.Bold else FontWeight.Normal
                        }
                    )
                )}
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    // Handle swipe gestures to change tabs
                    detectHorizontalDragGestures { change, dragAmount ->
                        if (dragAmount > 30) {
                            selectedTabIndex = 0
                        } else if (dragAmount < -30) {
                            selectedTabIndex = 1
                        }
                    }
                }
        ) {
            if (selectedTabIndex == 0) {
                // Hasn't loaded, display loading spinner
                if (!areTicketsLoaded.value) {
                    LoadingSpinner()
                } else if (ticketsState.isEmpty()) {
                    // Display no tickets
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "No tickets available", style = TextStyle(
                                fontFamily = marcherFontFamily,
                                color = Color.White,
                                fontSize = MaterialTheme.typography.titleMedium.fontSize
                            )
                        )
                    }
                } else {
                    // Display tickets
                    // Use the ticket fragment to display the tickets
                    TicketsTab(
                        ctx = ctx,
                        tickets = filteredTickets,
                        onFilterChanged = { isChecked ->
                            // if checked, shows only active vouchers, else shows all vouchers
                            filteredTickets = if (isChecked) {
                                ticketsState.filter { !it.isUsed }
                            } else {
                                ticketsState
                            }
                        }
                    )
                }
            }
            else if (selectedTabIndex == 1) {
                OrdersTab(
                    ctx = ctx,
                    onFilterChanged = {/*TODO*/}
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


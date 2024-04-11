package org.feup.carlosverissimo3001.theaterpal.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.feup.carlosverissimo3001.theaterpal.api.getUserOrders
import org.feup.carlosverissimo3001.theaterpal.api.getUserTickets
import org.feup.carlosverissimo3001.theaterpal.auth.Authentication
import org.feup.carlosverissimo3001.theaterpal.marcherFontFamily
import org.feup.carlosverissimo3001.theaterpal.models.OrderRcv
import org.feup.carlosverissimo3001.theaterpal.models.Ticket
import org.feup.carlosverissimo3001.theaterpal.nfc.buildTicketMessage
import org.feup.carlosverissimo3001.theaterpal.screens.fragments.Wallet.Orders.NoOrders
import org.feup.carlosverissimo3001.theaterpal.screens.fragments.Wallet.Tickets.NoTickets
import org.feup.carlosverissimo3001.theaterpal.screens.fragments.Wallet.Orders.OrdersTab
import org.feup.carlosverissimo3001.theaterpal.screens.fragments.Wallet.Tickets.SendTicketsActivity
import org.feup.carlosverissimo3001.theaterpal.screens.fragments.Wallet.Tickets.TicketsTab
import org.feup.carlosverissimo3001.theaterpal.screens.fragments.Wallet.Transactions.TransactionsFragment

@Composable
fun Wallet(ctx: Context, navController: NavController) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val areTicketsLoaded = remember { mutableStateOf(false) }

    var ticketsState by remember { mutableStateOf(emptyList<Ticket>()) }
    var filteredTickets by remember { mutableStateOf(emptyList<Ticket>()) }
    /*var groupedTickets by remember { mutableStateOf(emptyList<Ticket>()) }*/

    var orders by remember { mutableStateOf(emptyList<OrderRcv>()) }
    var areOrdersLoaded by remember { mutableStateOf(false) }

    var isValidatingTickets by remember { mutableStateOf(false) }

    var isViewingTransactions by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        getUserTickets(user_id = Authentication(ctx).getUserID()) { tickets ->
            ticketsState = tickets
            areTicketsLoaded.value = true

            // filter out used tickets
            filteredTickets = tickets.filter { !it.isUsed }

            /*// group tickets with the same show and date
            groupedTickets = groupTickets(tickets)*/

            getUserOrders(user_id = Authentication(ctx).getUserID()) { fetchedOrders ->
                orders = fetchedOrders
                areOrdersLoaded = true
            }
        }
    }

    if (!isViewingTransactions) {
        Column(modifier = Modifier.fillMaxSize()) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = {
                        Text("Tickets",
                            style = TextStyle(
                                fontFamily = marcherFontFamily,
                                color = Color.White,
                                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                fontWeight = (selectedTabIndex == 0).let {
                                    if (it) FontWeight.Bold else FontWeight.Normal
                                }
                            )
                        )
                    }
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = {
                        Text("Cafeteria Orders", style =
                        TextStyle(
                            fontFamily = marcherFontFamily,
                            color = Color.White,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            fontWeight = (selectedTabIndex == 1).let {
                                if (it) FontWeight.Bold else FontWeight.Normal
                            }
                        )
                        )
                    }
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        // Handle swipe gestures to change tabs
                        detectHorizontalDragGestures { _, dragAmount ->
                            if (dragAmount > 30) {
                                selectedTabIndex = 0
                            } else if (dragAmount < -30) {
                                selectedTabIndex = 1
                            }
                        }
                    }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedTabIndex == 0) {
                        // Hasn't loaded, display loading spinner
                        if (!areTicketsLoaded.value) {
                            LoadingSpinner()
                        } else if (ticketsState.isEmpty()) {
                            NoTickets(
                                onClick = {
                                    navController.navigate("shows")
                                }
                            )
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
                                },
                                onValidate = { ticketsToSend ->
                                    isValidatingTickets = true

                                    // tickets to nfc message
                                    val ticketMessage = buildTicketMessage(ticketsToSend, ctx)

                                    // create intent and put the message and tickets
                                    val intent = Intent(context, SendTicketsActivity::class.java)
                                    intent.putExtra("message", ticketMessage)
                                    intent.putExtra("valuetype", 1)
                                    intent.putParcelableArrayListExtra(
                                        "tickets",
                                        ArrayList(ticketsToSend)
                                    )

                                    context.startActivity(intent)
                                },
                                onConsultTransactionsClicked = {
                                    isViewingTransactions = true
                                }
                            )
                        }
                    } else if (selectedTabIndex == 1) {
                        if (!areOrdersLoaded)
                            LoadingSpinner()
                        else if (orders.isEmpty()) {
                            NoOrders(
                                onClick = {
                                    navController.navigate("cafeteria")
                                }
                            )
                        } else {
                            OrdersTab(
                                ctx,
                                orders,
                                onConsultTransactionsClicked = {
                                    isViewingTransactions = true
                                }
                            )
                        }
                    }
                }
            }
        }
    } else {
        TransactionsFragment(
            ctx,
            onBackButtonClick = {
                isViewingTransactions = false
            },
            onClick = { route ->
                navController.navigate(route)
            }
        )
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


package org.feup.carlosverissimo3001.theaterpal.screens.fragments.wallet.transactions

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.feup.carlosverissimo3001.theaterpal.api.getUserTransactions
import org.feup.carlosverissimo3001.theaterpal.auth.Authentication
import org.feup.carlosverissimo3001.theaterpal.file.saveTicketsToCache
import org.feup.carlosverissimo3001.theaterpal.file.saveVouchersToCache
import org.feup.carlosverissimo3001.theaterpal.marcherFontFamily
import org.feup.carlosverissimo3001.theaterpal.models.Auxiliary.createDefaultTransaction
import org.feup.carlosverissimo3001.theaterpal.models.Parser
import org.feup.carlosverissimo3001.theaterpal.models.Parser.parseTransaction
import org.feup.carlosverissimo3001.theaterpal.models.Ticket
import org.feup.carlosverissimo3001.theaterpal.models.Voucher
import org.feup.carlosverissimo3001.theaterpal.models.transaction.Transaction
import org.feup.carlosverissimo3001.theaterpal.screens.LoadingSpinner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsFragment(
    ctx: Context,
    onBackButtonClick: () -> Unit,
    onClick: (String) -> Unit
){
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
        rememberTopAppBarState()
    )

    var transactions by remember { mutableStateOf(emptyList<Transaction>()) }
    var areTransactionsLoaded by remember { mutableStateOf(false) }

    var isViewingDetails by remember {
        mutableStateOf(false)
    }

    val transactionDefault : Transaction = createDefaultTransaction()
    var selectedTransaction by remember { mutableStateOf(transactionDefault) }

    LaunchedEffect(Unit) {
        getUserTransactions(Authentication(ctx).getUserID()) { data ->
            val transactionsArr = data.getJSONArray("transactions")
            val ticketsArr = data.getJSONArray("tickets")
            val vouchersArr = data.getJSONArray("vouchers")

            val transactionList = mutableListOf<Transaction>()
            val ticketsList = mutableListOf<Ticket>()
            val vouchersList = mutableListOf<Voucher>()

            for (i in 0 until data.getJSONArray("transactions").length()) {
                val transaction = transactionsArr.getJSONObject(i)

                transactionList.add(parseTransaction(transaction))
            }

            for (i in 0 until data.getJSONArray("tickets").length()) {
                val ticket = ticketsArr.getJSONObject(i)

                ticketsList.add(Parser.parseTicket(ticket))
            }

            for (i in 0 until data.getJSONArray("vouchers").length()) {
                val voucher = vouchersArr.getJSONObject(i)

                vouchersList.add(Parser.parseVoucher(voucher))
            }


            transactions = transactionList
            areTransactionsLoaded = true

            transactions = transactions.sortedBy { it.timestamp }

            // replaces the local info in the cache
            saveTicketsToCache(ticketsList, ctx) {
                if (!it){
                    Log.e("TransactionsFragment", "Failed to save tickets to cache")
                }
            }

            saveVouchersToCache(vouchersList, ctx) {
                if (!it) {
                    Log.e("TransactionsFragment", "Failed to save vouchers to cache")
                }
            }
        }
    }

    Column {
        CenterAlignedTopAppBar(
            title = {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Transactions",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        fontFamily = marcherFontFamily
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            navigationIcon = {
                IconButton(onClick = onBackButtonClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Localized description"
                    )
                }
            },
            scrollBehavior = scrollBehavior,
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
        ){
            if (!areTransactionsLoaded){
                LoadingSpinner()
            }
            else if (transactions.isEmpty()){
                NoTransactions(
                    onClick = onClick
                )
            }

            else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    contentPadding = PaddingValues(10.dp)
                ) {
                    items(transactions.size) { index ->
                        Transaction(
                            ctx,
                            transaction = transactions[index],
                            onClick = {
                                println("Transaction clicked")
                                selectedTransaction = transactions[index]
                                isViewingDetails = true
                            }
                        )
                    }
                }
            }

            TransactionDetails(
                name = Authentication(ctx).getUserName(),
                nif = Authentication(ctx).getUserNIF(),
                transaction = selectedTransaction,
                onCancel = {
                    isViewingDetails = false
                },
                isInspecting = isViewingDetails
            )
        }
    }
}
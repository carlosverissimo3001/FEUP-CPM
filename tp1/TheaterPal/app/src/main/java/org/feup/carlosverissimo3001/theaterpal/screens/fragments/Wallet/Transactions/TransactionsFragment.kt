package org.feup.carlosverissimo3001.theaterpal.screens.fragments.Wallet.Transactions

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.feup.carlosverissimo3001.theaterpal.api.getUserTransactions
import org.feup.carlosverissimo3001.theaterpal.auth.Authentication
import org.feup.carlosverissimo3001.theaterpal.marcherFontFamily
import org.feup.carlosverissimo3001.theaterpal.models.Ticket
import org.feup.carlosverissimo3001.theaterpal.models.Transaction
import org.feup.carlosverissimo3001.theaterpal.models.createDefaultTransaction
import org.feup.carlosverissimo3001.theaterpal.screens.LoadingSpinner
import org.feup.carlosverissimo3001.theaterpal.screens.fragments.Wallet.Orders.Order

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
        getUserTransactions(Authentication(ctx).getUserID()) { fetchedTransactions ->
            transactions = fetchedTransactions
            areTransactionsLoaded = true

            transactions = transactions.sortedBy { it.timestamp }
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
package org.feup.carlosverissimo3001.theaterpal.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import org.feup.carlosverissimo3001.theaterpal.api.getUserVouchers
import org.feup.carlosverissimo3001.theaterpal.auth.Authentication
import org.feup.carlosverissimo3001.theaterpal.marcherFontFamily
import org.feup.carlosverissimo3001.theaterpal.models.Voucher
import org.feup.carlosverissimo3001.theaterpal.screens.fragments.Cafeteria.BarTab
import org.feup.carlosverissimo3001.theaterpal.screens.fragments.Cafeteria.PastVouchers
import org.feup.carlosverissimo3001.theaterpal.screens.fragments.Cafeteria.VouchersTab
import org.feup.carlosverissimo3001.theaterpal.screens.fragments.Wallet.PastTickets

@Composable
fun Cafeteria(ctx: Context) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    var vouchersState = remember { mutableStateOf(emptyList<Voucher>()) }
    var areVouchersLoaded = remember { mutableStateOf(false) }

    val viewingPastVouchers = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        getUserVouchers(user_id = Authentication(ctx).getUserID()) { vouchers ->
            vouchersState.value = vouchers
            areVouchersLoaded.value = true
        }
    }

    val vouchersArray = vouchersState.value
    val usedVouchersArray = vouchersArray.filter { it.isUsed }
    val unusedVouchersArray = vouchersArray.filter { !it.isUsed }

    if (!viewingPastVouchers.value) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary,
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 },
                text = {
                    Text("Bar",
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
                    Text("Vouchers", style =
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
                    BarTab(ctx)
                } else {
                    VouchersTab(
                        ctx = ctx,
                        vouchers = unusedVouchersArray,
                        onViewPastVouchersClick = {
                            viewingPastVouchers.value = true
                        },
                    )
                }
            }
        }
    }
    else {
        AnimatedVisibility(
            visible = viewingPastVouchers.value,
            enter = slideInVertically { it },
        ) {
            PastVouchers(ctx = ctx, pastVouchers = usedVouchersArray, onBackButtonClick = {
                viewingPastVouchers.value = false
            })
        }
    }
}
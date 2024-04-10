package org.feup.carlosverissimo3001.theaterpal.screens

import android.content.Context
import android.nfc.NfcAdapter
import android.widget.Toast
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import org.feup.carlosverissimo3001.theaterpal.api.getUserVouchers
import org.feup.carlosverissimo3001.theaterpal.api.submitOrder
import org.feup.carlosverissimo3001.theaterpal.auth.Authentication
import org.feup.carlosverissimo3001.theaterpal.marcherFontFamily
import org.feup.carlosverissimo3001.theaterpal.models.BarOrder
import org.feup.carlosverissimo3001.theaterpal.models.Order
import org.feup.carlosverissimo3001.theaterpal.models.Voucher
import org.feup.carlosverissimo3001.theaterpal.models.setTotal
import org.feup.carlosverissimo3001.theaterpal.screens.fragments.Cafeteria.BarTab
import org.feup.carlosverissimo3001.theaterpal.screens.fragments.Cafeteria.SendingOrderFragment
import org.feup.carlosverissimo3001.theaterpal.screens.fragments.Cafeteria.VouchersTab
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream

@Composable
fun Cafeteria(ctx: Context) {
    val nfcAdapter = NfcAdapter.getDefaultAdapter(ctx)

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val areVouchersLoaded = remember { mutableStateOf(false) }
    var isChoosingVoucher by remember { mutableStateOf(false) }

    var isSendingOrder by remember { mutableStateOf(false) }

    var vouchersState by remember { mutableStateOf(emptyList<Voucher>()) }
    var filteredVouchers by remember { mutableStateOf(emptyList<Voucher>()) }

    var barOrder by remember { mutableStateOf<BarOrder?>(null) }
    var order by remember { mutableStateOf<Order?>(null) }

    LaunchedEffect(Unit) {
        getUserVouchers(user_id = Authentication(ctx).getUserID()) { vouchers ->
            vouchersState = vouchers
            areVouchersLoaded.value = true
            filteredVouchers = vouchers.filter { !it.isUsed }
        }
    }

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
                isChoosingVoucher = false
                BarTab(
                    ctx,
                    onNextStepClick = { order ->
                        // Navigate to next step
                        barOrder = order
                        isChoosingVoucher = true
                        selectedTabIndex = 1
                    },
                )
            } else {
                if (!areVouchersLoaded.value) {
                    LoadingSpinner()
                }
                else {
                    VouchersTab(
                        ctx,
                        vouchers = if (isChoosingVoucher) vouchersState.filter { !it.isUsed } else filteredVouchers,
                        onFilterChanged = { isChecked ->
                            // if checked, shows only active vouchers, else shows all vouchers
                            filteredVouchers = if (isChecked) {
                                vouchersState.filter { !it.isUsed }
                            } else {
                                vouchersState
                            }
                        },
                        isChoosingVoucher,
                        onSubmitted = { selectedVouchers, updatedTotal ->
                            // update total, user might have selected vouchers for discount
                            setTotal(barOrder!!, updatedTotal)

                            // create order
                            order = barOrder?.let {
                                Order(
                                    barOrder = it,
                                    vouchersUsed = selectedVouchers
                                )
                            }

                            isSendingOrder = true

                            if (nfcAdapter == null){
                                Toast.makeText(ctx, "NFC is not supported on this device", Toast.LENGTH_SHORT).show()
                            }
                            else if (!nfcAdapter.isEnabled){
                                Toast.makeText(ctx, "Please enable NFC", Toast.LENGTH_SHORT).show()
                            }
                            else {
                                // TODO : Activate NFC
                                sendOrder(ctx, order!!)

                                // Navigate to next step
                                /*isChoosingVoucher = false
                                selectedTabIndex = 0*/
                            }
                        },
                        total = barOrder?.total ?: 0.00
                    )
                }

                SendingOrderFragment(
                    isSending = isSendingOrder,
                    onCancel = {
                        isSendingOrder = false
                    },
                    order = order
                )
            }
        }
    }
}

fun sendOrder(ctx: Context, order: Order) {
    /*submitOrder(ctx, order = order, callback = { success ->
        if (success) {
            println("Order submitted successfully")
        } else {
            println("Error submitting order")
        }
    })*/

    /*try {
        // Serialize the Order object into a byte array
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(order)
        val orderByteArray = byteArrayOutputStream.toByteArray()

        // Send the orderByteArray through NFC
        // Example: NFC.sendData(orderByteArray)

        // Close the streams
        objectOutputStream.close()
        byteArrayOutputStream.close()

        println("Order sent successfully through NFC.")
    } catch (e: Exception) {
        println("Error sending order through NFC: $e")
    }
    println("I'm here")*/
}

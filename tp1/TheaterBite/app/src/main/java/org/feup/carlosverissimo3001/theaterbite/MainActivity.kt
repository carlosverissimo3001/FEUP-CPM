package org.feup.carlosverissimo3001.theaterbite

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.*

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import org.feup.carlosverissimo3001.theaterbite.api.*
import org.feup.carlosverissimo3001.theaterbite.models.*
import org.feup.carlosverissimo3001.theaterbite.nfc.NFCReader
import org.feup.carlosverissimo3001.theaterbite.screens.CafeteriaTerminalScreen
import java.security.PublicKey


class MainActivity : AppCompatActivity() {
    private val nfc by lazy { NfcAdapter.getDefaultAdapter(applicationContext) }
    private var nfcReader : NFCReader? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        nfc.disableReaderMode(this)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                Color.Transparent.toArgb()
            ),
            navigationBarStyle = SystemBarStyle.dark(
                Color.Transparent.toArgb()
            )
        )

        setContent {
            CafeteriaTerminalScreen(
                onStartScan = {
                    enableNFCReaderMode()
                },
                onDismissRequest = {
                    disableNFCReaderMode()
                },
                loading = false
            )
        }
    }

    private fun enableNFCReaderMode() {
        // Initialize NFC reader if not already initialized
        if (nfcReader == null) {
            nfcReader = NFCReader(::nfcReceived)
        }
        // Enable NFC reader mode
        nfc?.enableReaderMode(this, nfcReader, READER_FLAGS, null)
    }

    private fun disableNFCReaderMode() {
        // Disable NFC reader mode if initialized
        nfc?.disableReaderMode(this)
    }

    private fun nfcReceived(type: Int, content: ByteArray) {
        setContent {
            CafeteriaTerminalScreen(
                onStartScan = {
                    enableNFCReaderMode()
                },
                onDismissRequest = {
                    disableNFCReaderMode()
                },
                loading = true
            )
            when (type) {
                1 -> {
                    parseContent(content)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        setContent {
            CafeteriaTerminalScreen(
                onStartScan = {
                    enableNFCReaderMode()
                },
                onDismissRequest = {
                    disableNFCReaderMode()
                },
                loading = false
            )
        }

        nfc.enableReaderMode(this, nfcReader, READER_FLAGS, null)
    }

    override fun onPause() {
        super.onPause()
        nfc.disableReaderMode(this)
    }

    private fun parseContent(content: ByteArray){
        // get the userid
        val useridlength = content[1].toInt()
        val userid  = String(content.sliceArray(2..useridlength+1))

        // get the public key
        var publicKeyB64: String
        var publicKey : PublicKey
        var signatureVerified = false

        getPublicKey(userid) {
            publicKeyB64 = it

            // Decode the public key into a PublicKey object
            publicKey = decodePublicKey(publicKeyB64)

            // Verify the signature
            if (verifySignature(content, publicKey)){
                signatureVerified = true
            }
        }

       while (!signatureVerified)
           Thread.sleep(10) // Avoid busy waiting

        // get the ids of the vouchers used
        val (vouchersUsed, currIndex) = extractVouchers(content)

        // get the products
        val (products, nextIndex) = extractProducts(content, currIndex)

        // get the order amount
        val orderAmountLength = content[nextIndex].toInt()
        val orderAmount = String(content.sliceArray(nextIndex + 1 until nextIndex + 1 + orderAmountLength)).toDouble()

        val order = Order(products, orderAmount, vouchersUsed)

        // Send the order to the server
        val confirmedOrder = sendOrder(userid, order)

        val intent = Intent(this, OrderConfirmationActivity::class.java)

        intent.putExtra("order", confirmedOrder)
        startActivity(intent)
    }
}

fun extractVouchers(content: ByteArray) : Pair<List<String>, Int> {
    val numberOfVouchersIdx = 2 + content[1].toInt()
    val numberOfVouchers = content[numberOfVouchersIdx].toInt()

    val vouchers = mutableListOf<String>()
    var currIndex = numberOfVouchersIdx + 1

    for (i in 0 until numberOfVouchers){
        // Constants.UUID_SIZE is 36, so we extract 36 bytes for each voucher
        val voucherIdx = numberOfVouchersIdx + 1 + i * Constants.UUID_SIZE
        val voucher = String(content.sliceArray(voucherIdx until voucherIdx + Constants.UUID_SIZE))
        vouchers.add(voucher)

        currIndex += Constants.UUID_SIZE
    }

    return Pair(vouchers, currIndex)
}

fun extractProducts(content: ByteArray, idx: Int) : Pair<List<Product>,Int>{
    val products = mutableListOf<Product>()
    val numProducts = content[0]
    var currIndex = idx

    for (i in 0 until numProducts){
        // NAME
        val productNameLength = content[currIndex++].toInt()
        val productName = String(content.sliceArray(currIndex until currIndex + productNameLength))
        currIndex += productNameLength

        // PRICE
        val productPriceLength = content[currIndex++].toInt()
        val productPrice = String(content.sliceArray(currIndex until currIndex + productPriceLength)).toDouble()
        currIndex += productPriceLength

        // Quantity
        val productQuantity = content[currIndex++].toInt()

        products.add(Product(productName, productPrice, productQuantity))
    }

    return Pair(products, currIndex)
}


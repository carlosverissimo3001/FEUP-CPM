package org.feup.carlosverissimo3001.theaterbite

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.feup.carlosverissimo3001.theaterbite.api.APILayer
import org.feup.carlosverissimo3001.theaterbite.models.Order
import org.feup.carlosverissimo3001.theaterbite.models.Product
import java.nio.ByteBuffer
import java.security.Signature


class MainActivity : AppCompatActivity() {
    private val nfc by lazy { NfcAdapter.getDefaultAdapter(applicationContext) }
    private val nfcReader by lazy { NFCReader(::nfcReceived) }
    private var apiLayer = APILayer(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                Color.Transparent.toArgb()
            ),
            navigationBarStyle = SystemBarStyle.dark(
                Color.Transparent.toArgb()
            )
        )

        setContent {
            CafeteriaTerminal()
        }
    }

    private fun nfcReceived(type: Int, content: ByteArray) {
        runOnUiThread {
            when (type) {
                1 -> parseContent(content)
            }
        }
    }

    override fun onResume() {
        super.onResume()
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

        // Verify the signature
        if (!verifySignature(content, userid)){
            return
        }

        // get the ids of the vouchers used
        val (vouchersUsed, currIndex) = extractVouchers(content)

        // get the products
        val (products, nextIndex) = extractProducts(content, currIndex)

        // get the order amount
        val orderAmountLength = content[nextIndex].toInt()
        val orderAmount = String(content.sliceArray(nextIndex + 1 until nextIndex + 1 + orderAmountLength)).toDouble()

        val order = Order(products, orderAmount, vouchersUsed)

        // Submit the order
        //order.orderNo = submitOrder(userid, order)

        val intent = Intent(this, OrderConfirmationActivity::class.java)
    }

    private fun verifySignature(content: ByteArray, userid: String): Boolean {
        var publicKey = ""

        var validated = false
        var isDoneValidating = false

        val bb = ByteBuffer.wrap(content)
        val sign = ByteArray(Constants.KEY_SIZE / 8)
        val mess = ByteArray(content.size - sign.size)

        // message
        bb.get(mess, 0, mess.size)

        // constant, 64 bytes (512/8)
        bb.get(sign, 0, Constants.KEY_SIZE/8)

        // retrieve the public key from the server
        apiLayer.getPublicKey(userid) {
            publicKey = it

            val pkey = decodePublicKey(publicKey)

            try{
                validated = Signature.getInstance(Constants.SIGN_ALGO).run {
                    initVerify(pkey)
                    update(mess)
                    verify(sign)
                }
            } catch (ex: Exception) {
                println("Error verifying signature: ${ex.message}")
            }

            isDoneValidating = true

            println("Signature verified: $validated")
        }

        while (!isDoneValidating){
            Thread.sleep(100) // wait for the public key to be retrieved and to validate the signature
        }

        return validated
    }

    fun submitOrder(userid: String, order: Order) : Int{
        var isOrderSubmitted = false
        var orderNo = 0

        apiLayer.submitOrder(userid, order){
            orderNo = it
            isOrderSubmitted = true
        }

        while (!isOrderSubmitted){
            Thread.sleep(100)
        }

        return orderNo
    }
}

fun extractVouchers(content: ByteArray) : Pair<List<String>, Int> {
    val numberOfVouchersIdx = 2 + content[1].toInt()
    var numberOfVouchers = content[numberOfVouchersIdx].toInt()

    var vouchers = mutableListOf<String>()
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
    var products = mutableListOf<Product>()
    var numProducts = content[0]
    var currIndex = idx

    for (i in 0 until numProducts){
        // NAME
        var productNameLength = content[currIndex++].toInt()
        var productName = String(content.sliceArray(currIndex until currIndex + productNameLength))
        currIndex += productNameLength

        // PRICE
        var productPriceLength = content[currIndex++].toInt()
        var productPrice = String(content.sliceArray(currIndex until currIndex + productPriceLength)).toDouble()
        currIndex += productPriceLength

        // Quantity
        var productQuantity = content[currIndex++].toInt()

        products.add(Product(productName, productPrice, productQuantity))
    }

    return Pair(products, currIndex)
}

@Composable
fun CafeteriaTerminal()
{
    var showBottomSheet by remember { mutableStateOf(false) }
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CafeteriaTerminalTopBar()
        CafeteriaTerminalDescription(
            str = "Please approach your device to scan your order."
        )
        CafeteriaTerminalImage()
        CafeteriaTerminalSubmitButton("Scan Order"){
            // API LAYER STUFF HERE
            // ACTIVATE SCANNING OF NFC DEVICE
            showBottomSheet = true
        }
        if (showBottomSheet)
            CafeteriaTerminalBottomSheet(
                imageID = R.drawable.nfc_scanning,
                description = "Scanning your device...",
                onDismissRequest = {
                    showBottomSheet = false
                },
            )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CafeteriaTerminalTopBar() {
    CenterAlignedTopAppBar(
        modifier = Modifier.
            padding(top = 25.dp, bottom = 15.dp),
        title = {
            Text("Validate your order",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 33.sp,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )
        },

        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
        ),
    )
}

@Composable
fun CafeteriaTerminalDescription(str: String)
{
    Text(
        text = str,
        modifier = Modifier
            .padding(top = 0.dp, bottom = 5.dp, start = 60.dp, end = 60.dp),
        style = TextStyle(
            color = Color(0x77FFFFFF),
            fontSize = 14.sp,
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Normal
        ),
        textAlign = TextAlign.Center
    )
}

@Composable
fun CafeteriaTerminalImage()
{
    Image(
        painter = painterResource(id = R.drawable.nfc_action),
        contentDescription = null,
        modifier = Modifier
            .padding(top = 100.dp, bottom = 20.dp)
            .border(5.dp, Color.White, CircleShape)
            .background(Color.Gray, CircleShape)
            .fillMaxWidth(0.8f)
            .aspectRatio(1f)
            .clip(CircleShape)
    )
}

@Composable
fun CafeteriaTerminalSubmitButton(str: String, onClick: () -> Unit)
{
    Button(
        modifier = Modifier
            .padding(top = 50.dp, bottom = 20.dp)
            .fillMaxWidth(0.8f),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xBB00AA66),
        ),
        onClick = onClick
    ) {
        Text(
            text = str,
            style = TextStyle(
                color = Color.White,
                fontSize = 20.sp,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CafeteriaTerminalBottomSheet(
    imageID: Int,
    description: String,
    onDismissRequest: () -> Unit
)
{
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { onDismissRequest() },
        sheetState = sheetState,
        containerColor = Color(0xFF1F1F1F),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = imageID),
                contentDescription = null,
                modifier = Modifier
                    .padding(10.dp)
                    .clip(CircleShape)
                    .fillMaxWidth(0.3f)
                    .aspectRatio(1f)
            )
            Text(
                text = description,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 15.sp,
                    fontFamily = poppinsFontFamily,
                ),
                modifier = Modifier
                    .padding(5.dp)
            )
            Spacer(
                modifier = Modifier.padding(bottom = 20.dp)
            )
        }
    }
}

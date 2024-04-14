package org.feup.carlosverissimo3001.theaterpal.screens.fragments.Cafeteria

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import org.feup.carlosverissimo3001.theaterpal.Constants
import org.feup.carlosverissimo3001.theaterpal.models.Order
import org.feup.carlosverissimo3001.theaterpal.models.printOrder
import org.feup.carlosverissimo3001.theaterpal.nfc.Card
import org.feup.carlosverissimo3001.theaterpal.nfc.isNfcEnabled
import org.feup.carlosverissimo3001.theaterpal.nfc.isNfcavailable


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

class SendOrderActivity : AppCompatActivity() {
    private lateinit var orderToSend: Order

    private val broadcastReceiver = object: BroadcastReceiver() {
        override fun onReceive(ctx: Context, intent: Intent) {
            Toast.makeText(this@SendOrderActivity, "NFC link lost", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        orderToSend = intent.parcelable("order")!!

        printOrder(orderToSend)

        // check if NFC is available
        if (!isNfcavailable(this)) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show()
            finish()
        }
        else if (!isNfcEnabled(this)) {
            // Prompt user to enable NFC
            Toast.makeText(this, "Please enable NFC", Toast.LENGTH_LONG).show()

            finish()
        }

        setContent {
            SendingOrderFragment(
                ctx = this,
                isSending = true,
                onCancel = {
                    finish()
                },
                order = orderToSend
            )
        }
    }

    override fun onResume() {
        super.onResume()
        Card.contentMessage = intent.getByteArrayExtra("message") ?: ByteArray(0)       // message to send via card emulation
        Card.type = intent.getIntExtra("valuetype", 0) // 1 for tickets
        val intentFilter = IntentFilter(Constants.ACTION_CARD_DONE)
        LocalBroadcastManager.getInstance(applicationContext).registerReceiver(broadcastReceiver, intentFilter)  // to receive 'link loss'
    }

    override fun onPause() {
        super.onPause()
        Card.type = 0  // allow sending only when this Activity is running
        LocalBroadcastManager.getInstance(applicationContext).unregisterReceiver(broadcastReceiver)
    }
}
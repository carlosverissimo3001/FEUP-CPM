package org.feup.carlosverissimo3001.theaterpal.screens.fragments.Wallet.Tickets

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import org.feup.carlosverissimo3001.theaterpal.Constants
import org.feup.carlosverissimo3001.theaterpal.models.Ticket
import org.feup.carlosverissimo3001.theaterpal.nfc.Card
import org.feup.carlosverissimo3001.theaterpal.nfc.isNfcEnabled
import org.feup.carlosverissimo3001.theaterpal.nfc.isNfcavailable

@Suppress("DEPRECATION")
inline fun <reified T: Parcelable>Intent.getParcelableArrayListExtraProvider(identifierParameter: String): java.util.ArrayList<T>? {
    return if (Build.VERSION.SDK_INT >= 33) {
        this.getParcelableArrayListExtra(identifierParameter, T::class.java)
    } else {
        this.getParcelableArrayListExtra(identifierParameter)
    }
}

class SendTicketsActivity : AppCompatActivity() {
    private lateinit var ticketsToSend: List<Ticket>

    private val broadcastReceiver = object: BroadcastReceiver() {
        override fun onReceive(ctx: Context, intent: Intent) {
            Toast.makeText(this@SendTicketsActivity, "NFC link lost", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ticketsToSend = intent.getParcelableArrayListExtraProvider<Ticket>("tickets")!!

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
            SendingTicketsFragment(
                ctx = this,
                isValidating = true,
                onCancel = {
                    finish()
                },
                tickets = ticketsToSend
            )
        }
    }

    override fun onResume() {
        super.onResume()
        Card.contentMessage = intent.getByteArrayExtra("message") ?: ByteArray(0)       // message to send via card emulation
        Card.type = intent.getIntExtra("valuetype", 0) // 1 for tickets
        val intentFilter = IntentFilter(Constants.ACTION_CARD_DONE)
        LocalBroadcastManager.getInstance(applicationContext).registerReceiver(broadcastReceiver, intentFilter)  // to receive 'link loss'

        println("Message: ${Card.contentMessage}")
    }

    override fun onPause() {
        super.onPause()
        Card.type = 0  // allow sending only when this Activity is running
        LocalBroadcastManager.getInstance(applicationContext).unregisterReceiver(broadcastReceiver)
    }
}
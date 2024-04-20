package org.feup.carlosverissimo3001.theaterpal.screens.fragments.wallet.tickets

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import org.feup.carlosverissimo3001.theaterpal.BottomBarTheme
import org.feup.carlosverissimo3001.theaterpal.Constants
import org.feup.carlosverissimo3001.theaterpal.file.setTicketsAsUsed
import org.feup.carlosverissimo3001.theaterpal.getParcelableArrayListExtraProvider
import org.feup.carlosverissimo3001.theaterpal.models.Ticket
import org.feup.carlosverissimo3001.theaterpal.nfc.Card
import org.feup.carlosverissimo3001.theaterpal.nfc.isNfcEnabled
import org.feup.carlosverissimo3001.theaterpal.nfc.isNfcavailable

class SendTicketsActivity : AppCompatActivity() {
    private lateinit var ticketsToSend: List<Ticket>

    private val broadcastReceiver = object: BroadcastReceiver() {
        override fun onReceive(ctx: Context, intent: Intent) {
            // Mark tickets as used in the cache
            setTicketsAsUsed(ticketsToSend, ctx) {
                Toast.makeText(this@SendTicketsActivity, "Tickets sent successfully", Toast.LENGTH_LONG).show()
                finish()
            }
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
            BottomBarTheme(){
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background){
                    SendingTicketsFragment(
                        ctx = this,
                        isSending = true,
                        onCancel = {
                            finish()
                        },
                        tickets = ticketsToSend
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Card.contentMessage = intent.getByteArrayExtra("message") ?: ByteArray(0)       // message to send via card emulation
        Card.type = intent.getIntExtra("valuetype", 0) // 1 for tickets
        val intentFilter = IntentFilter(Constants.ACTION_CARD_DONE)
        LocalBroadcastManager.getInstance(applicationContext).registerReceiver(broadcastReceiver, intentFilter)  // to receive 'link loss'

        // println("Message: ${Card.contentMessage}")
    }

    override fun onPause() {
        super.onPause()
        Card.type = 0  // allow sending only when this Activity is running
        LocalBroadcastManager.getInstance(applicationContext).unregisterReceiver(broadcastReceiver)
    }
}
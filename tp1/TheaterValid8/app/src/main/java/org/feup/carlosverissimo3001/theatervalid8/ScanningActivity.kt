package org.feup.carlosverissimo3001.theatervalid8

import android.nfc.NfcAdapter
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.feup.carlosverissimo3001.theatervalid8.api.APILayer
import org.feup.carlosverissimo3001.theatervalid8.fragments.NfcIsScanningFragment

class ScanningActivity : AppCompatActivity() {
    private val isSuccessfulScan : Boolean = false
    private val nfc by lazy { NfcAdapter.getDefaultAdapter(applicationContext) }
    private val nfcReader by lazy { NFCReader(::nfcReceived) }
    private var apiLayer = APILayer(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent (
            content = {
                val (isScanning, setIsScanning) = remember { mutableStateOf(true) }


                NfcIsScanningFragment(
                    isScanning = isScanning,
                    onCancel = {
                        // Stop scanning
                        setIsScanning(false)

                        finish()
                    }
                )
            }
        )
    }

    private fun nfcReceived(type: Int, content: ByteArray) {
        runOnUiThread {
            when (type) {
                1 -> validateTickets(content)
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

    private fun validateTickets(content: ByteArray) {
        val numberOfTickets = content[0].toInt()
        println("Number of tickets: $numberOfTickets")

        // Get the user id
        val useridlength = content[1].toInt()
        val userid  = String(content.sliceArray(2..useridlength+1))

        var publicKey = ""

        apiLayer.getPublicKey(userid) {
            publicKey = it
        }

        // Validate the ticket
        // validateTicket(ticket)
        println("Ticket validated")
    }
}
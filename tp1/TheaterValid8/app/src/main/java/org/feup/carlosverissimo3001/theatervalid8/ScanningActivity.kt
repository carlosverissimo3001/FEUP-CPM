package org.feup.carlosverissimo3001.theatervalid8

import android.nfc.NfcAdapter
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.feup.carlosverissimo3001.theatervalid8.api.APILayer
import org.feup.carlosverissimo3001.theatervalid8.fragments.NfcFailureFragment
import org.feup.carlosverissimo3001.theatervalid8.fragments.NfcIsScanningFragment
import org.feup.carlosverissimo3001.theatervalid8.fragments.NfcSuccessFragment
import org.feup.carlosverissimo3001.theatervalid8.models.Ticket
import java.nio.ByteBuffer
import java.security.PublicKey
import java.security.Signature

class ScanningActivity : AppCompatActivity() {
    private var isSuccessfullScan : Boolean = false
    private var isError : Boolean = false
    private var error : String = ""

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

                if (isSuccessfullScan){
                    setIsScanning(false)
                    NfcSuccessFragment(
                        onDone = {
                            // Finish the activity
                            finish()
                        }
                    )
                }

                if (isError) {
                    setIsScanning(false)

                    NfcFailureFragment(
                        error = error,
                        onRetry = {
                            // Try again
                            setIsScanning(true)
                            isError = false
                        }
                    )
                }
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
        // Check message.txt for the format of the message
        val useridlength = content[1].toInt()
        val userid  = String(content.sliceArray(2..useridlength+1))

        val (tickets, currIndex) = extractTicketsFromMessage(content)
        var publicKey = ""

        var validated = false

        val bb = ByteBuffer.wrap(content)
        val sign = ByteArray(Constants.KEY_SIZE / 8)
        val mess = ByteArray(content.size - sign.size)

        // message -> 0 to
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
                isError = true
                error =
                    """
                  ${ex.message}
                """.trimIndent()
            }

            println("Signature verified: $validated")
        }

        // Validate the ticket
        // validateTicket(ticket)
        println("Ticket validated")
        isSuccessfullScan = true

        setContent(
            content = {
                NfcSuccessFragment(
                    onDone = {
                        finish()
                    }
                )
            }
        )
    }

    private fun extractTicketsFromMessage(content: ByteArray): Pair<MutableList<Ticket>, Int> {
        // Start index: 2 (number of tickets + useridlength) + useridlength
        val numberOfTickets = content[0]
        var currentIndex = 2 + content[1].toInt()

        val tickets = mutableListOf<Ticket>()

        for (i in 0 until numberOfTickets) {
            // TICKETID
            val ticketIdLength = content[currentIndex++]
            val ticketId = String(content.sliceArray(currentIndex until currentIndex + ticketIdLength))
            currentIndex += ticketIdLength.toInt()

            // USERID
            val userIdLength = content[currentIndex++]
            val userId = String(content.sliceArray(currentIndex until currentIndex + userIdLength))
            currentIndex += userIdLength.toInt()

            // SHOWNAME
            val showNameLength = content[currentIndex++]
            val showName = String(content.sliceArray(currentIndex until currentIndex + showNameLength))
            currentIndex += showNameLength.toInt()

            // SEAT
            val seatLength = content[currentIndex++]
            val seat = String(content.sliceArray(currentIndex until currentIndex + seatLength))
            currentIndex += seatLength.toInt()

            // IS USED
            val isUsed = content[currentIndex++] == 1.toByte()

            // DATE
            val dateLength = content[currentIndex++]
            val date = String(content.sliceArray(currentIndex until currentIndex + dateLength))
            currentIndex += dateLength.toInt()

            // Create Ticket object and add it to the list
            val ticket = Ticket(
                ticketId,
                userId,
                showName,
                seat,
                isUsed,
                date,
                "",
            )
            tickets.add(ticket)
        }


        return Pair(tickets, currentIndex)
    }
}
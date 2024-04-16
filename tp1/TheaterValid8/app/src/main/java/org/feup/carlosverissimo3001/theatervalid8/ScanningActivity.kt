package org.feup.carlosverissimo3001.theatervalid8

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.feup.carlosverissimo3001.theatervalid8.api.APILayer
import org.feup.carlosverissimo3001.theatervalid8.models.show.*
import org.feup.carlosverissimo3001.theatervalid8.screens.NfcIsScanningFragment
import org.feup.carlosverissimo3001.theatervalid8.models.Ticket
import org.feup.carlosverissimo3001.theatervalid8.screens.ValidationStatusActivity
import java.nio.ByteBuffer
import java.security.Signature


class ScanningActivity : AppCompatActivity() {
    private lateinit var show : Show
    private lateinit var showDate : ShowDate

    private var isError : Boolean = false
    private var error : String = ""

    private val nfc by lazy { NfcAdapter.getDefaultAdapter(applicationContext) }
    private val nfcReader by lazy { NFCReader(::nfcReceived) }
    private var apiLayer = APILayer(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        show = intent.parcelable("show")!!
        showDate = intent.parcelable("showDate")!!

        setContent (
            content = {
                NfcIsScanningFragment(
                    isScanning = true,
                    onCancel = {
                        finish()
                    },
                    showName = show.name,
                    date = showDate.date
                )
            }
        )
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

    private fun parseContent(content: ByteArray) {
        val useridlength = content[1].toInt()
        val userid  = String(content.sliceArray(2..useridlength+1))

        val tickets  = extractTicketsFromMessage(content)
        var publicKey: String

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

        // Validate the tickets
        val ticketsState = validateTickets(userid, tickets)

        // show that status of the validation in the next activity
        val intent = Intent(this, ValidationStatusActivity::class.java)
        intent.putExtra("tickets", ArrayList(ticketsState))
        intent.putExtra("show", show.name)
        intent.putExtra("date", showDate.date)

        startActivity(intent)
    }

    private fun extractTicketsFromMessage(content: ByteArray): MutableList<Ticket> {
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
                date
            )
            tickets.add(ticket)
        }


        return tickets
    }

    private fun validateTickets(userid: String, tickets: MutableList<Ticket>) : List<Ticket> {
        val wrongDateTickets = mutableListOf<Ticket>()
        //val states = mutableListOf<TicketState>()

        for (t in tickets) {
            if (show.name != t.showName){
                t.isValidated = false
                t.stateDesc = "Wrong show"

                wrongDateTickets.add(t)
            }

            else if (showDate.date != t.date){
                t.isValidated = false
                t.stateDesc = "Wrong date"

                wrongDateTickets.add(t)
            }
        }

        // remove the tickets with the wrong date
        tickets.removeAll(wrongDateTickets)

        // validate the rest of the tickets with the server
        var isValidationComplete = false
        apiLayer.validateTicketsWithServer(userid, tickets.map { it.ticketid }) {
            for (i in it.indices) {
                // update the tickets with the new state
                tickets[i].isValidated = it[i].state == "Ticket validated!"
                tickets[i].stateDesc = it[i].state
            }

            isValidationComplete = true
        }

        while (!isValidationComplete) {
            // wait for the validation to complete
            Thread.sleep(100) // Avoid busy waiting
        }

        return tickets + wrongDateTickets
    }
}
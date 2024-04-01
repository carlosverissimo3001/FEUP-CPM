package org.feup.carlosverissimo3001.theatervalid8

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.feup.carlosverissimo3001.theatervalid8.fragments.*
import org.feup.carlosverissimo3001.theatervalid8.models.*
import org.feup.carlosverissimo3001.theatervalid8.screens.*

const val READER_FLAGS = NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

class ValidatorActivity : AppCompatActivity() {
    private lateinit var show : Show
    private lateinit var showDate : ShowDate
    private var validationStatus: Boolean = false
    private var isScanning: Boolean = false
    //private val nfc by lazy { NfcAdapter.getDefaultAdapter(applicationContext) }
    //private val nfcReader by lazy { NFCReader(::nfcReceived) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        show = intent.parcelable("show")!!
        showDate = intent.parcelable("showDate")!!

        setContent(
            content = {
                val (isScanning, setIsScanning) = remember { mutableStateOf(false) }

                Column {
                    ValidatorScreen(
                        applicationContext,
                        show,
                        showDate,
                        onBackButtonClick = {
                            // Finish the activity
                            finish()
                        },
                        onValidate = {
                            // Validate the ticket
                            // validateTicket(ticket)
                        },
                        isScanning = isScanning
                    )

                    NfcIsScanningFragment(
                        isScanning = isScanning,
                        onCancel = {
                            // Stop scanning
                            setIsScanning(false)
                        }
                    )

                    if (!isScanning){
                        NfcValidatorFragment(
                            onScanButtonClick = {
                                // Start scanning
                                setIsScanning(true)

                                // TODO : ACTIVATE NFC
                            }
                        )
                    }
                }
            }
        )
    }

    /*override fun onResume() {
        super.onResume()
        nfc.enableReaderMode(this, nfcReader, READER_FLAGS, null)
    }

    override fun onPause() {
        super.onPause()
        nfc.disableReaderMode(this)
    }*/

    // callback to receive a key or product list from the NFC reader
    private fun nfcReceived(type: Int, content: ByteArray) {
        runOnUiThread {
            //
        }
    }
}







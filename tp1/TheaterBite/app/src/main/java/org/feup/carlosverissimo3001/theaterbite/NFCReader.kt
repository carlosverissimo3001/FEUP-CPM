package org.feup.carlosverissimo3001.theaterbite

import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.util.Log
import java.io.IOException

private const val CARD_AID = "F010203040"
private const val CMD_SEL_AID = "00A40400"
private val RES_OK_SW = hexStringToByteArray("9000")

class NFCReader(private val listener: (Int, ByteArray)->Unit) : NfcAdapter.ReaderCallback {
    override fun onTagDiscovered(tag: Tag) {
        val isoDep = IsoDep.get(tag)                    // Android smartcard reader emulator
        if (isoDep != null) {
            try {
                isoDep.connect()                            // establish a connection with the card and send 'select aid' command
                val result = isoDep.transceive(hexStringToByteArray(CMD_SEL_AID + String.format("%02X", CARD_AID.length/2) + CARD_AID))
                val rLen = result.size
                val status = byteArrayOf(result[rLen-2], result[rLen-1])
                println("Status: ${byteArrayToHex(status)}")
                if (RES_OK_SW.contentEquals(status)) {
                    listener(
                        result[0].toInt(),
                        result.sliceArray(1..rLen - 3)
                    )      // received a key (type == 1) or a list (type == 2)
                }
            } catch (e: IOException) {
                Log.e("CardReader", "Error communicating with card: $e")
            }
        }
    }
}
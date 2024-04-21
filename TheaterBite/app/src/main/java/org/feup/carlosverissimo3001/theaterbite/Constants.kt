package org.feup.carlosverissimo3001.theaterbite

import android.nfc.NfcAdapter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Coffee
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight

const val READER_FLAGS = NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK

object Constants {
    const val URL = "https://open-blowfish-cleanly.ngrok-free.app"
    const val UUID_SIZE = 36
    const val KEY_SIZE = 512
    const val SIGN_ALGO = "SHA256WithRSA"
}

var itemIcons = mapOf(
    "Popcorn" to Icons.Outlined.Fastfood,
    "Soda" to Icons.Outlined.Fastfood,
    "Coffee" to Icons.Outlined.Coffee,
    "Sandwich" to Icons.Outlined.Fastfood,
    "Free Coffee" to Icons.Outlined.Coffee,
    "Free Popcorn" to Icons.Outlined.Fastfood
)

val poppinsFontFamily = androidx.compose.ui.text.font.FontFamily(
    Font(R.font.poppins_light, FontWeight.W300),
    Font(R.font.poppins_regular, FontWeight.W400),
    Font(R.font.poppins_medium, FontWeight.W500),
    Font(R.font.poppins_semibold, FontWeight.W600),
    Font(R.font.poppins_bold, FontWeight.W700),
    Font(R.font.poppins_black, FontWeight.W900),
)

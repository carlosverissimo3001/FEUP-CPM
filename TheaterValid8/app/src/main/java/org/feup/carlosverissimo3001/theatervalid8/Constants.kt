package org.feup.carlosverissimo3001.theatervalid8

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

object Constants {
    const val KEY_SIZE = 512
    const val ANDROID_KEYSTORE = "AndroidKeyStore"
    const val KEY_ALGO = "RSA"
    const val SIGN_ALGO = "SHA256WithRSA"
    const val KEY_NAME = "user_auth_key"
    const val URL = "https://open-blowfish-cleanly.ngrok-free.app"
    /* const val URL = "https://wrongly-in-pug.ngrok-free.app" */
    const val ACTION_CARD_DONE = "CMD_PROCESSING_DONE"
}

val showNameImageMap = mutableMapOf<String, String>()

val poppinsFontFamily = FontFamily(
    Font(R.font.poppins_light, FontWeight.W300),
    Font(R.font.poppins_regular, FontWeight.W400),
    Font(R.font.poppins_medium, FontWeight.W500),
    Font(R.font.poppins_semibold, FontWeight.W600),
    Font(R.font.poppins_bold, FontWeight.W700),
    Font(R.font.poppins_black, FontWeight.W900),
)


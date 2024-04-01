package org.feup.carlosverissimo3001.theaterpal

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

object Crypto {
    const val KEY_SIZE = 512
    const val ANDROID_KEYSTORE = "AndroidKeyStore"
    const val KEY_ALGO = "RSA"
    const val KEY_NAME = "user_auth_key"
}

object Server {
    const val URL = "https://44f4-161-230-86-77.ngrok-free.app"
}

val marcherFontFamily: FontFamily = FontFamily(
    Font(R.font.marcher_light, FontWeight.Light),
    Font(R.font.marcher_bold, FontWeight.Bold),
)
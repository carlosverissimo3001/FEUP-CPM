package org.feup.carlosverissimo3001.theaterpal


import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.*

val showNameImageMap = mutableMapOf<String, String>()
object Constants {
    const val KEY_SIZE = 512
    const val ANDROID_KEYSTORE = "AndroidKeyStore"
    const val KEY_ALGO = "RSA"
    const val SIGN_ALGO = "SHA256WithRSA"
    const val KEY_NAME = "user_auth_key"
    const val URL = "https://open-blowfish-cleanly.ngrok-free.app"
    const val ACTION_CARD_DONE = "CMD_PROCESSING_DONE"
    const val UUID_SIZE = 36
}

object MyColors
{
    val tertiaryColor: Color = Color(0xFFF54242)
    val bottomNavBarUnselectedItemColor: Color = Color(0x88FFFFFF)
}


val marcherFontFamily: FontFamily = FontFamily(
    Font(R.font.marcher_light, FontWeight.Light),
    Font(R.font.marcher_bold, FontWeight.Bold),
)




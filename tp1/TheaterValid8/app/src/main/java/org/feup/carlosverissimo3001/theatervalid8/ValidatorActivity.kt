package org.feup.carlosverissimo3001.theatervalid8

import android.content.Intent
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import org.feup.carlosverissimo3001.theatervalid8.models.*
import org.feup.carlosverissimo3001.theatervalid8.screens.NfcValidatorFragment
import org.feup.carlosverissimo3001.theatervalid8.screens.ValidatorScreen

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

class ValidatorActivity : AppCompatActivity() {
    private lateinit var show : Show
    private lateinit var showDate : ShowDate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        show = intent.parcelable("show")!!
        showDate = intent.parcelable("showDate")!!

        setContent(
            content = {
                ValidatorScreen(
                    applicationContext,
                    show,
                    showDate,
                    onBackButtonClick = {
                        // Finish the activity
                        finish()
                    }
                )
            }
        )
    }
}







package org.feup.carlosverissimo3001.theaterbite

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.substring
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.*
import org.feup.carlosverissimo3001.theaterbite.models.ConfirmedOrder
import org.feup.carlosverissimo3001.theaterbite.models.Order
import org.feup.carlosverissimo3001.theaterbite.screens.OrderConfirmationScreen

class OrderConfirmationActivity : AppCompatActivity() {
    private val delayMillis = 10000L // 10 seconds
    private lateinit var job: Job
    private lateinit var order: ConfirmedOrder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        order = intent.parcelable("order")!!

        job = CoroutineScope(Dispatchers.Main).launch {
            delay(delayMillis)
            finish()
        }

        setContent {
            OrderConfirmationScreen(
                order,
                seconds = 10
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel() // Cancel the coroutine to avoid leaks
    }
}






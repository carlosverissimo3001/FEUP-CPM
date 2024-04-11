package org.feup.carlosverissimo3001.theaterbite

import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                Color.Transparent.toArgb()
            ),
            navigationBarStyle = SystemBarStyle.dark(
                Color.Transparent.toArgb()
            )
        )

        setContent {
            CafeteriaTerminal()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CafeteriaTerminal()
{
    var showBottomSheet by remember { mutableStateOf(false) }
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CafeteriaTerminalTopBar()
        CafeteriaTerminalDescription(
            str = "Please approach your device to scan your order."
        )
        CafeteriaTerminalImage()
        CafeteriaTerminalSubmitButton("Scan Order"){
            // API LAYER STUFF HERE
            // ACTIVATE SCANNING OF NFC DEVICE
            showBottomSheet = true
        }
        if (showBottomSheet)
            CafeteriaTerminalBottomSheet(
                imageID = R.drawable.nfc_scanning,
                description = "Scanning your device...",
                onDismissRequest = {
                    showBottomSheet = false
                },
            )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CafeteriaTerminalTopBar() {
    CenterAlignedTopAppBar(
        modifier = Modifier.
            padding(top = 25.dp, bottom = 15.dp),
        title = {
            Text("Validate your order",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 33.sp,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )
        },

        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
        ),
    )
}

@Composable
fun CafeteriaTerminalDescription(str: String)
{
    Text(
        text = str,
        modifier = Modifier
            .padding(top = 0.dp, bottom = 5.dp, start = 60.dp, end = 60.dp),
        style = TextStyle(
            color = Color(0x77FFFFFF),
            fontSize = 14.sp,
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Normal
        ),
        textAlign = TextAlign.Center
    )
}

@Composable
fun CafeteriaTerminalImage()
{
    Image(
        painter = painterResource(id = R.drawable.nfc_action),
        contentDescription = null,
        modifier = Modifier
            .padding(top = 100.dp, bottom = 20.dp)
            .border(5.dp, Color.White, CircleShape)
            .background(Color.Gray, CircleShape)
            .fillMaxWidth(0.8f)
            .aspectRatio(1f)
            .clip(CircleShape)
    )
}

@Composable
fun CafeteriaTerminalSubmitButton(str: String, onClick: () -> Unit)
{
    Button(
        modifier = Modifier
            .padding(top = 50.dp, bottom = 20.dp)
            .fillMaxWidth(0.8f),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xBB00AA66),
        ),
        onClick = onClick
    ) {
        Text(
            text = str,
            style = TextStyle(
                color = Color.White,
                fontSize = 20.sp,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CafeteriaTerminalBottomSheet(
    imageID: Int,
    description: String,
    onDismissRequest: () -> Unit
)
{
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { onDismissRequest() },
        sheetState = sheetState,
        containerColor = Color(0xFF1F1F1F),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = imageID),
                contentDescription = null,
                modifier = Modifier
                    .padding(10.dp)
                    .clip(CircleShape)
                    .fillMaxWidth(0.3f)
                    .aspectRatio(1f)
            )
            Text(
                text = description,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 15.sp,
                    fontFamily = poppinsFontFamily,
                ),
                modifier = Modifier
                    .padding(5.dp)
            )
            Spacer(
                modifier = Modifier.padding(bottom = 20.dp)
            )
        }
    }
}

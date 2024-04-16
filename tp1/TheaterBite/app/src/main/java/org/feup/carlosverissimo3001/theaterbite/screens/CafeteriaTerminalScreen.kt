package org.feup.carlosverissimo3001.theaterbite.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import org.feup.carlosverissimo3001.theaterbite.R
import org.feup.carlosverissimo3001.theaterbite.poppinsFontFamily

@Composable
fun CafeteriaTerminalScreen(onStartScan: () -> Unit, onDismissRequest: () -> Unit, loading: Boolean)
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
            showBottomSheet = true
            onStartScan()
        }
        if (showBottomSheet)
            CafeteriaTerminalBottomSheet(
                imageID = R.drawable.nfc_scanning,
                description = "Scanning your device...",
                onDismissRequest = {
                    showBottomSheet = false
                    onDismissRequest()
                },
                loading = loading
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
fun CafeteriaTerminalBottomSheet(imageID: Int, description: String, onDismissRequest: () -> Unit, loading: Boolean)
{
    val sheetState = rememberModalBottomSheetState()
    println("IS LOADING: $loading")

    ModalBottomSheet(
        onDismissRequest = { onDismissRequest() },
        sheetState = sheetState,
        containerColor = Color(0xFF1F1F1F),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (!loading)
                Image(
                    painter = painterResource(id = imageID),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(10.dp)
                        .clip(CircleShape)
                        .fillMaxWidth(0.3f)
                        .aspectRatio(1f)
                )

            else
                LoadingSpinner()

            Text(
                text = if (loading) "Please wait..." else description,
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


@Composable
fun LoadingSpinner() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
        CircularProgressIndicator(
            color = Color.White,
            strokeWidth = 2.dp
        )
    }
}
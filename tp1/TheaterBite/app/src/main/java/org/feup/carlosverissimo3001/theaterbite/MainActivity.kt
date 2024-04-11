package org.feup.carlosverissimo3001.theaterbite

import android.os.Bundle
import android.view.View
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults

import androidx.compose.runtime.Composable
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


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                Color.Transparent.toArgb()
            ),
        )

        setContentView(R.layout.activity_main)

        setContent {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CafeterialTerminalTopBar()
                CafeteriaTerminalDescription(
                    str = "Please approach your device to scan your order."
                )
                CafeteriaTerminalImage()
                CafeteriaTerminalSubmitButton("Scan Order") {

                }
            }
        }
    }
}



@Composable
fun LoadingSpinner() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            color = Color.White,
            strokeWidth = 2.dp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CafeterialTerminalTopBar() {
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
        onClick = onClick,
        modifier = Modifier
            .padding(top = 50.dp, bottom = 20.dp)
            .fillMaxWidth(0.8f),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xBB22FF22),
        )
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

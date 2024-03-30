package org.feup.carlosverissimo3001.theatervalid8.fragments

import android.graphics.Color.parseColor
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import okhttp3.internal.parseCookie
import org.feup.carlosverissimo3001.theatervalid8.R
import org.feup.carlosverissimo3001.theatervalid8.poppinsFontFamily

@Composable
fun NfcSuccessFragment(
    onCancel: () -> Unit = {}
) {
    val numTicketsValidated = 1

    Box (
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .border(
                width = 2.dp,
                color = androidx.compose.ui.graphics.Color.Gray,
                shape = RoundedCornerShape(15.dp)
            )
            .background(Color(android.graphics.Color.parseColor("#302c2c")), RoundedCornerShape(15.dp))
    ){
        Spacer(modifier = Modifier.size(10.dp))
        Column (
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ){
            Image(
                painter = painterResource(id = R.drawable.nfc_success),
                contentDescription = "NFC Success",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(150.dp)
            )

            Text(
                text = "Success",
                style = TextStyle(
                    fontFamily = poppinsFontFamily,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.ExtraBold,
                    color = androidx.compose.ui.graphics.Color.White,
                ),
            )

            Text(
                text = if (numTicketsValidated == 1)
                    "1 ticket was validated successfully!" else
                        "$numTicketsValidated tickets were validated!",
                style = TextStyle(
                    fontFamily = poppinsFontFamily,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    fontWeight = FontWeight.Normal,
                    color = androidx.compose.ui.graphics.Color.White,
                ),
                color = Color.White
            )
        }
    }
}
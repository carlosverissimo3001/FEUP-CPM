package org.feup.carlosverissimo3001.theaterbite.fragments

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.feup.carlosverissimo3001.theaterbite.R
import org.feup.carlosverissimo3001.theaterbite.poppinsFontFamily

@Composable
fun NfcValidatorFragment(
    onScanButtonClick: () -> Unit = {}
) {
    Box (
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .border(
                width = 2.dp,
                color = Color.Gray,
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
            Text(
                text = "Validate your ticket(s)",
                style = MaterialTheme.typography.titleLarge,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
            )
            Text(
                text = "Please approach your device to validate your theater ticket(s).",
                fontFamily = poppinsFontFamily,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White,
                textAlign = TextAlign.Center,
            )

            Image(
                painter = painterResource(id = R.drawable.nfc_action),
                contentDescription = "NFC action",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .border(3.dp, androidx.compose.ui.graphics.Color.White, CircleShape)
            )

            Button(
                onClick = { onScanButtonClick() },
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color (android.graphics.Color.parseColor("#50bc64")),
                    contentColor = androidx.compose.ui.graphics.Color.White
                )
            ) {
                Text(
                    "Scan",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}
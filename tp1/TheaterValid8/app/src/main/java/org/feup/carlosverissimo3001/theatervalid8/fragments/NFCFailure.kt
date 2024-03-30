package org.feup.carlosverissimo3001.theatervalid8.fragments

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import org.feup.carlosverissimo3001.theatervalid8.R
import org.feup.carlosverissimo3001.theatervalid8.poppinsFontFamily

@Composable
fun NfcFailureFragment(
    onCancel: () -> Unit = {}
) {
    val errorMessage : String = "This ticket is not valid anymore."

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
                painter = painterResource(id = R.drawable.nfc_failure),
                contentDescription = "NFC Error",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(150.dp)
            )

            Text(
                text = "Oops",
                style = TextStyle(
                    fontFamily = poppinsFontFamily,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                ),
            )

            Text(
                text = errorMessage,
                style = TextStyle(
                    fontFamily = poppinsFontFamily,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                ),
                color = Color.White
            )
        }
    }
}
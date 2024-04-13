package org.feup.carlosverissimo3001.theatervalid8.screens

import android.graphics.Color.parseColor
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.feup.carlosverissimo3001.theatervalid8.LoadingSpinner
import org.feup.carlosverissimo3001.theatervalid8.R
import org.feup.carlosverissimo3001.theatervalid8.formatDate
import org.feup.carlosverissimo3001.theatervalid8.poppinsFontFamily



@Composable
fun NfcIsScanningFragment(
    isScanning: Boolean,
    onCancel: () -> Unit,
    showName: String,
    date: String
) {
    AnimatedVisibility (
        visible = isScanning,
        enter = slideInVertically(
            // Slide in from the bottom
            initialOffsetY = { it },
            animationSpec = tween(
                durationMillis = 500,
                easing = FastOutSlowInEasing
            )
        ),
        exit = slideOutVertically(
            // Slide out to the bottom
            targetOffsetY = { it },
            animationSpec = tween(
                durationMillis = 500,
                easing = FastOutSlowInEasing
            )
        )
    ) {
        Box(
            modifier = Modifier
                .background(Color(parseColor("#302c2c")), RoundedCornerShape(15.dp))
                .pointerInput(Unit) {
                    // draw down == go back
                    detectDragGestures { _, dragAmount ->
                        if (dragAmount.y > 50) {
                            onCancel()
                        }
                    }
                }
        ){
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .height(6.dp)
                        .background(Color.White, CircleShape)
                        .pointerInput(Unit) {
                            detectDragGestures { _, dragAmount ->
                                if (dragAmount.y > 0) {
                                    // Dragging down, cancel the scanning
                                    onCancel()
                                }
                            }
                        }
                )

                Spacer(modifier = Modifier.size(5.dp))

                Column (
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ){
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Validating for: ",
                            style = TextStyle(
                                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                fontFamily = poppinsFontFamily,
                            ),
                            color = Color.White,
                        )
                        Text(
                            text = showName,
                            style = TextStyle(
                                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                            ),
                            color = Color.White,
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Date: ",
                            style = TextStyle(
                                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                fontFamily = poppinsFontFamily,
                            ),
                            color = Color.White,
                        )
                        Text(
                            text = formatDate(date),
                            style = TextStyle(
                                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                            ),
                            color = Color.White,
                        )
                    }
                }


                Image(
                    painter = painterResource(id = R.drawable.nfc_scanning),
                    contentDescription = "NFC action",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(25.dp)
                )

                Spacer(modifier = Modifier.size(5.dp))


                Text(
                    text = "Scanning for your tickets...",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontFamily = poppinsFontFamily,
                    ),
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                )

                Text(
                    text = "(Make sure to turn on NFC on your device)",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        fontFamily = poppinsFontFamily,
                    ),
                    color = Color.White,
                )

                Button(
                    onClick = { onCancel() },
                    modifier = Modifier
                        .fillMaxWidth(0.8f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Cancel",
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            fontFamily = poppinsFontFamily,
                        ),
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                }

            }
        }
    }
}
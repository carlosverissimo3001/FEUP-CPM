package org.feup.carlosverissimo3001.theaterpal.screens.fragments.wallet.tickets

import android.content.Context
import android.graphics.Bitmap
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.feup.carlosverissimo3001.theaterpal.R
import org.feup.carlosverissimo3001.theaterpal.file.loadImageFromCache
import org.feup.carlosverissimo3001.theaterpal.marcherFontFamily
import org.feup.carlosverissimo3001.theaterpal.models.Ticket

@Composable
fun SendingTicketsFragment(
    ctx: Context,
    isSending: Boolean,
    onCancel: () -> Unit = {},
    tickets : List<Ticket> = emptyList(),
) {
    AnimatedVisibility (
        visible = isSending,
        enter = slideInVertically(
            // Slide in from the bottom
            initialOffsetY = { it },
            animationSpec = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        ),
        exit = slideOutVertically(
            // Slide out to the bottom
            targetOffsetY = { it },
            animationSpec = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        ),
    ) {
        Box(
            modifier = Modifier
                .background(
                    Color(android.graphics.Color.parseColor("#302c2c")),
                    RoundedCornerShape(15.dp)
                )
                .fillMaxSize().pointerInput(Unit) {
                    // draw down == go back
                    detectDragGestures { change, dragAmount ->
                        if (dragAmount.y > 50) {
                            onCancel()
                        }
                    }
                }
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
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
            }

            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = "Validating ${tickets.size} ticket" + (if (tickets.size > 1) "s" else ""),
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontFamily = marcherFontFamily,
                        fontWeight = FontWeight.Bold,
                    ),
                    color = Color.White,
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    contentPadding = PaddingValues(10.dp),
                ) {
                    items(tickets.size) { index ->
                        val imagePath = tickets[index].imagePath
                        val bitmap: Bitmap? = loadImageFromCache(imagePath, ctx)

                        TicketValidate(
                            ticket = tickets[index],
                            image = bitmap,
                        )
                    }
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Please head to the validation terminal and tap your device",
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            fontFamily = marcherFontFamily,
                        ),
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                    )

                    Spacer(modifier = Modifier.size(20.dp))

                    Image(
                        painter = painterResource(id = R.drawable.nfc_scanning),
                        contentDescription = "NFC action",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxHeight(0.5f)
                    )

                    Spacer(modifier = Modifier.size(20.dp))

                    Button(
                        onClick = { onCancel() },
                        modifier = Modifier
                            .fillMaxWidth(0.6f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        )
                    ) {
                        Text(
                            text = "< Take me back",
                            style = TextStyle(
                                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                fontFamily = marcherFontFamily,
                            ),
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                        )
                    }
                }


            }
        }
    }
}

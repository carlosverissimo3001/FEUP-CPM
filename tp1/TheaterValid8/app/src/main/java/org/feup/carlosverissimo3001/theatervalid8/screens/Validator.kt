package org.feup.carlosverissimo3001.theatervalid8.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color.parseColor
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.feup.carlosverissimo3001.theatervalid8.CenteredContent
import org.feup.carlosverissimo3001.theatervalid8.File.decodeBase64ToBitmap
import org.feup.carlosverissimo3001.theatervalid8.File.loadImageFromCache
import org.feup.carlosverissimo3001.theatervalid8.R
import org.feup.carlosverissimo3001.theatervalid8.TopCenteredContent
import org.feup.carlosverissimo3001.theatervalid8.models.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValidatorScreen(ctx: Context, show: Show, showDate: ShowDate, onBackButtonClick: () -> Unit) {
    val sampleActors = arrayOf("Jane Doe", "John Doe", "Alice Doe", "Bob Doe")

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
        rememberTopAppBarState()
    )

    val imageb64 = show.pictureBase64
    val bitmap : Bitmap?

    if (imageb64 == "") {
        // get image from cache
        bitmap = loadImageFromCache(show.picture, ctx)
    }
    else {
        bitmap = decodeBase64ToBitmap(imageb64)
    }

    Column {
        CenterAlignedTopAppBar(
            title = {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "SALA VIP",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "In Session",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            navigationIcon = {
                IconButton(onClick = onBackButtonClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Localized description"
                    )
                }
            },
            scrollBehavior = scrollBehavior,
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.padding(12.dp)
        ) {
                Box(
                    modifier = Modifier
                        .border(
                            width = 2.dp,
                            color = Color.Gray, // Border color
                            shape = RoundedCornerShape(8.dp) // Border shape
                        )
                        .fillMaxWidth(0.55f)
                        .fillMaxHeight(0.5f)
                ) {
                    bitmap?.asImageBitmap()?.let {
                        Image(
                            bitmap = it,
                            contentDescription = "Show image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                Spacer(modifier = Modifier.size(8.dp))

                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // Add a column to display the show name
                    Box(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(
                            text = show.name,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            softWrap = true,
                            style = MaterialTheme.typography.headlineLarge,
                            color = isSystemInDarkTheme().let {
                                if (it) Color.White else Color.Black
                            },
                        )
                    }
                    Spacer(modifier = Modifier.size(25.dp))

                    Text(
                        text = show.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = isSystemInDarkTheme().let {
                            if (it) Color.White else Color.Black
                        },
                    )

                    Spacer(modifier = Modifier.size(13.dp))

                    Row {
                        Text(
                            text = "Age Rating: ",
                            fontWeight = FontWeight.ExtraBold,
                            style = MaterialTheme.typography.bodySmall,
                            color = isSystemInDarkTheme().let {
                                if (it) Color.White else Color.Black
                            },
                        )
                        Text(
                            text = "16+",
                            style = MaterialTheme.typography.bodySmall,
                            color = isSystemInDarkTheme().let {
                                if (it) Color.White else Color.Black
                            },
                        )
                    }

                    Spacer(modifier = Modifier.size(10.dp))

                    Row {
                        Text(
                            text = "Director: ",
                            fontWeight = FontWeight.ExtraBold,
                            style = MaterialTheme.typography.bodySmall,
                            color = isSystemInDarkTheme().let {
                                if (it) Color.White else Color.Black
                            },
                        )
                        Text(
                            text = "Christopher Nolan",
                            style = MaterialTheme.typography.bodySmall,
                            color = isSystemInDarkTheme().let {
                                if (it) Color.White else Color.Black
                            },
                        )
                    }

                    Spacer(modifier = Modifier.size(10.dp))

                    Row {
                        Text(
                            text = "Price: ",
                            fontWeight = FontWeight.ExtraBold,
                            style = MaterialTheme.typography.bodySmall,
                            color = isSystemInDarkTheme().let {
                                if (it) Color.White else Color.Black
                            },
                        )
                        Text(
                            text = "${show.price}€",
                            style = MaterialTheme.typography.bodySmall,
                            color = isSystemInDarkTheme().let {
                                if (it) Color.White else Color.Black
                            },
                        )
                    }

                    Spacer(modifier = Modifier.size(10.dp))

                    Text(
                        text = "Cast: ",
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.bodySmall,
                        color = isSystemInDarkTheme().let {
                            if (it) Color.White else Color.Black
                        },
                    )

                    Column {
                        sampleActors.forEach {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodySmall,
                                color = isSystemInDarkTheme().let {
                                    if (it) Color.White else Color.Black
                                },
                            )
                        }
                    }

                    Spacer(modifier = Modifier.size(10.dp))

                    Spacer(modifier = Modifier.size(10.dp))

                    Row {
                        Text(
                            text = "Duration: ",
                            fontWeight = FontWeight.ExtraBold,
                            style = MaterialTheme.typography.bodySmall,
                            color = isSystemInDarkTheme().let {
                                if (it) Color.White else Color.Black
                            },
                        )
                        Text(
                            text = "150 minutes",
                            style = MaterialTheme.typography.bodySmall,
                            color = isSystemInDarkTheme().let {
                                if (it) Color.White else Color.Black
                            },
                        )
                    }

                }
            }
        NfcValidatorFragment()
    }
}

@Composable
fun NfcValidatorFragment() {
    Box (
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .background(Color(parseColor("#302c2c"))),
    ){
        Column (
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ){
            Text(
                text = "Validate your ticket",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
            )
            Text(
                text = "Please approach you device to validate your theatre ticket.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White
            )

            Box(
                modifier = Modifier
                    .border(
                        width = 2.dp,
                        color = Color.Gray, // Border color
                        shape = RoundedCornerShape(8.dp) // Border shape
                    )
                    .fillMaxWidth(0.55f)
                    .fillMaxHeight(0.5f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.nfc_action),
                    contentDescription = "NFC action",
                )
            }

            Button(
                onClick = { /* Do something! */ },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
            ) {
                Text("Validate Ticket")
            }
            Spacer(modifier = Modifier.size(10.dp))
        }
    }
}
package org.feup.carlosverissimo3001.theaterpal.screens.fragments.Shows

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.FlowRowScopeInstance.align
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import org.feup.carlosverissimo3001.theaterpal.api.getShows
import org.feup.carlosverissimo3001.theaterpal.file.loadImageFromCache
import org.json.JSONArray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Shows(ctx: Context) {
    val showsState = remember { mutableStateOf<JSONArray?>(null) }

    LaunchedEffect(Unit) {
        getShows(ctx) { shows ->
            showsState.value = shows
        }
    }

    val showsArray = showsState.value
    if (showsArray == null)
        LoadingSpinner()
    else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            //Grid of 2 x N show cards
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
            ){
                items(showsArray.length()) { index ->
                    val show = showsArray.getJSONObject(index)
                    val title = show.getString("name")
                    val description = show.getString("description")
                    val imagename = show.getString("picture")
                    val imageb64 = show.getString("picture_b64")

                    var showDates = show.getJSONArray("dates")

                    val bitmap: Bitmap?

                    if (imageb64 == ""){
                        bitmap = loadImageFromCache(imagename, ctx)
                    }

                    else {
                        bitmap = decodeBase64ToBitmap(imageb64)
                    }


                    ElevatedCard(
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 6.dp
                        ),
                        modifier = Modifier
                            .size(width = 160.dp, height = 240.dp)
                            .padding(8.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.BottomCenter
                        ){
                            bitmap?.asImageBitmap()?.let {
                                Image(
                                    bitmap = it,
                                    contentDescription = null,
                                    // keep the same size for all images
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            // Spacer(modifier = Modifier.weight(1f))

                            Text(
                                text = title,
                                style = MaterialTheme.typography.h6,
                                modifier = Modifier
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                MaterialTheme.colors.primary,
                                                MaterialTheme.colors.primaryVariant
                                            )
                                        )
                                    )
                                    .padding(8.dp)
                                    .fillMaxWidth()
                            )
                        }
                    }
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


fun decodeBase64ToBitmap(base64String: String): Bitmap? {
    val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
}




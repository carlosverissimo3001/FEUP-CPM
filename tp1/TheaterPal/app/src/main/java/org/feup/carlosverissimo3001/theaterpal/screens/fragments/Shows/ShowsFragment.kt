package org.feup.carlosverissimo3001.theaterpal.screens.fragments.Shows

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import org.feup.carlosverissimo3001.theaterpal.api.getShows
import org.feup.carlosverissimo3001.theaterpal.file.areShowsStoreInCache
import org.feup.carlosverissimo3001.theaterpal.file.loadImageFromCache
import org.feup.carlosverissimo3001.theaterpal.models.Show
import org.feup.carlosverissimo3001.theaterpal.screens.NavRoutes
import org.feup.carlosverissimo3001.theaterpal.file.loadShowsFromCache
import org.feup.carlosverissimo3001.theaterpal.file.saveShowsToCache
import org.feup.carlosverissimo3001.theaterpal.marcherFontFamily
import org.json.JSONArray

@Composable
fun Shows(ctx: Context, navController: NavController) {
    val showsState = remember { mutableStateOf<List<Show>?>(null) }
    val areShowsCached = remember { mutableStateOf(false)}

    LaunchedEffect(Unit) {
        getShows(ctx) { shows ->
            showsState.value = shows
        }
    }
//    if (!areShowsCached.value) {
//        LaunchedEffect(Unit) {
//            getShows(ctx) { shows ->
//                showsState.value = shows
//
//                saveShowsToCache(shows, ctx){success ->
//                    if (!success){
//                    }
//                    else{
//                        areShowsCached.value = true
//                    }
//                }
//            }
//        }
//    }
//    else {
//        LaunchedEffect(Unit) {
//            loadShowsFromCache(ctx) { shows ->
//                // The file exists, but is still empty
//                if (shows == emptyList<Show>()){
//                    showsState.value = null
//                }
//                else {
//                    showsState.value = shows
//                }
//            }
//        }
//    }

    val showList = showsState.value
    if (showList == null)
        LoadingSpinner()

    else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
            ){
                items(showList.size) { index ->
                    val show = showList[index]

                    val bitmap: Bitmap? = if (show.picture_b64 == "")
                        loadImageFromCache(show.picture, ctx)
                    else
                        decodeBase64ToBitmap(show.picture_b64)

                    ShowCard(show, bitmap, navController)
                }
            }
        }
    }
}

@Composable
fun ShowCard(show: Show, bitmap: Bitmap?, navController: NavController)  {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .size(width = 160.dp, height = 240.dp)
            .padding(8.dp),
        onClick = {
            navController.currentBackStackEntry?.savedStateHandle?.set("show", show)
            navController.navigate(NavRoutes.ShowDetails.route) {
                launchSingleTop = true
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
            }



        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ){
            Image(
                bitmap = bitmap!!.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Text(
                text = show.name,
                style = TextStyle(
                    color = Color.White,
                    fontFamily = marcherFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                ),
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            startY = 10f,
                            endY = 90f,
                            colors = listOf(
                                Color.Transparent,
                                Color.Black
                            )
                        )
                    )
                    .padding(8.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
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




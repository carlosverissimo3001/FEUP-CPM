package org.feup.carlosverissimo3001.theaterpal.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.FlowRowScopeInstance.align
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import org.feup.carlosverissimo3001.theaterpal.Server
import org.json.JSONArray
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Shows() {
    val showsState = remember { mutableStateOf<JSONArray?>(null) }

    LaunchedEffect(Unit) {
        getShows { shows ->
            showsState.value = shows.getJSONArray("shows")
        }
    }

    val showsArray = showsState.value
    if (showsArray == null)
        Text(text = "Loading...", color = Color.White)
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
                    val image = show.getString("picture")
                    val bitmap = decodeBase64ToBitmap(image)

                    ElevatedCard(
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 6.dp
                        ),
                        modifier = Modifier
                            .size(width = 160.dp, height = 240.dp)
                            .padding(8.dp)
                    )  {
                        Column (

                        ) {
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = title,
                                style = MaterialTheme.typography.h6,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
fun decodeBase64ToBitmap(base64String: String): Bitmap? {
    val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
}

fun getShows(callback: (JSONObject) -> Unit) {
    val client = OkHttpClient()

    val request = okhttp3.Request.Builder()
        .url("${Server.URL}/shows")
        .build()

    client.newCall(request).enqueue(object : okhttp3.Callback {
        override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
            e.printStackTrace()
            callback(JSONObject())
        }

        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
            when (response.code) {
                200 -> {
                    val responseBody = response.body?.string()
                    val jsonResponse = responseBody?.let { JSONArray(it) }
                    if (jsonResponse != null)
                        callback(JSONObject().put("shows", jsonResponse))
                }
                else -> {
                    print("Error getting shows")
                }
            }
        }
    })
}

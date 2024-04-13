package org.feup.carlosverissimo3001.theatervalid8.screens

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import org.feup.carlosverissimo3001.theatervalid8.file.decodeBase64ToBitmap
import org.feup.carlosverissimo3001.theatervalid8.file.loadImageFromCache
import org.feup.carlosverissimo3001.theatervalid8.models.*
import org.feup.carlosverissimo3001.theatervalid8.poppinsFontFamily
import org.feup.carlosverissimo3001.theatervalid8.screens.fragments.Show

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValidatorScreen(
    ctx: Context,
    show: Show,
    showDate: ShowDate,
    onBackButtonClick: () -> Unit,
    onValidate: () -> Unit,
    isScanning: Boolean)
{
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

    Column (
        modifier = if (isScanning) Modifier.alpha(0.3f) else Modifier.alpha(1.0f)
    ) {
        CenterAlignedTopAppBar(
            title = {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "SALA VIP",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        fontFamily = poppinsFontFamily,
                    )
                    Text(
                        text = "In Session",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        fontFamily = poppinsFontFamily,
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

        Show(
            show = show,
            bitmap = bitmap
        )
    }
}




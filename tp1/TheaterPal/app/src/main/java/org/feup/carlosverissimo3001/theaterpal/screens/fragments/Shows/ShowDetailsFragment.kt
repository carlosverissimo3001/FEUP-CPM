package org.feup.carlosverissimo3001.theaterpal.screens.fragments.Shows

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.feup.carlosverissimo3001.theaterpal.marcherFontFamily
import org.feup.carlosverissimo3001.theaterpal.models.Show

@Composable
fun ShowDetails(ctx: Context, navController: NavController) {
	val show: Show? = navController.previousBackStackEntry?.savedStateHandle?.get("show")
	val bitmap: Bitmap? = navController.previousBackStackEntry?.savedStateHandle?.get("bitmap")
	if (show != null && bitmap != null)
		ShowDetailsScreen(ctx, show, bitmap)

}

@Composable
fun ShowDetailsScreen(ctx: Context, show: Show, bitmap: Bitmap) {
	Box(
		contentAlignment = Alignment.BottomCenter,
	){
		Image(
			bitmap = bitmap!!.asImageBitmap(),
			contentDescription = null,
			contentScale = ContentScale.Crop,
			modifier = Modifier
				.fillMaxWidth(1f)

		)
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.background(
					brush = Brush.verticalGradient(
						startY = 0f,
						endY = Float.POSITIVE_INFINITY,
						colors = listOf(
							Color.Transparent,
							Color.Black
						)
					)
				)
		)
		Text(
			text = show.name,
			style = TextStyle(
				color = Color.White,
				fontFamily = marcherFontFamily,
				fontWeight = FontWeight.Bold,
				fontSize = MaterialTheme.typography.bodyLarge.fontSize
			),
			modifier = Modifier
				.padding(8.dp)
				.fillMaxWidth(),
			textAlign = TextAlign.Center
		)

	}
}
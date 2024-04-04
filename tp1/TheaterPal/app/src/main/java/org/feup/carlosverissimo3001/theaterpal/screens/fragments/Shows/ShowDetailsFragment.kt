package org.feup.carlosverissimo3001.theaterpal.screens.fragments.Shows

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
	LazyColumn {
		item {
			ShowImage(bitmap)
		}
		item {
			Column(
			) {
				ShowName(show)
//				ShowTags(show)
				ShowDescription(show)
			}
		}
	}
}

@Composable
fun ShowImage(bitmap: Bitmap) {
	Box(
		modifier = Modifier
			.aspectRatio(0.8f)
			.fillMaxWidth(),
		contentAlignment = Alignment.BottomCenter
	){
		Image(
			bitmap = bitmap.asImageBitmap(),
			contentDescription = null,
			contentScale = ContentScale.Crop,
			modifier = Modifier
				.fillMaxWidth()
		)
		Box(
			modifier = Modifier
				.matchParentSize()
				.background(
					brush = Brush.verticalGradient(
						colors = listOf(
							Color.Transparent,
							MaterialTheme.colorScheme.background
						),
						tileMode = TileMode.Clamp,
					)
				)
		)
//		Box(
//			modifier = Modifier
//				.matchParentSize()
//				.background(
//					brush = Brush.horizontalGradient(
//						colors = listOf(
//							MaterialTheme.colorScheme.background,
//							Color.Transparent,
//							MaterialTheme.colorScheme.background
//						),
//						tileMode = TileMode.Clamp
//					)
//				)
//		)
	}
}

@Composable
fun ShowName(show: Show) {
	Text(
		text = show.name,
		style = TextStyle(
			color = Color.White,
			fontFamily = marcherFontFamily,
			fontWeight = FontWeight.Bold,
			fontSize = 36.sp
		),
		modifier = Modifier
			.padding(top = 16.dp, bottom = 8.dp)
			.fillMaxWidth(),
		textAlign = TextAlign.Center
	)
	Text(
		text = "01-01-2001 • 20h00 • 90 min",
		style = TextStyle(
			color = Color(0x66FFFFFF),
			fontFamily = marcherFontFamily,
			fontSize = 13.sp
		),
		modifier = Modifier
			.padding(bottom = 16.dp)
			.fillMaxWidth(),
		textAlign = TextAlign.Center
	)
}

@Composable
fun ShowDescription(show: Show) {
	Text(
		text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce vulputate varius nisl, vel ultricies ligula facilisis id. Sed interdum magna in odio commodo, ac condimentum est vulputate. Vestibulum sed vestibulum dui. Ut sit amet arcu lobortis, interdum neque dictum, dignissim risus. Sed fermentum feugiat sagittis. Quisque elementum quam nec fringilla venenatis. Integer gravida nibh et sem euismod dignissim. Morbi finibus ut turpis at venenatis. Nullam in varius sem, sed tincidunt ex. Phasellus luctus sagittis turpis sit amet accumsan. Nunc congue ultrices fermentum. Vestibulum viverra faucibus diam, in dapibus massa interdum sit amet. Nam sodales, urna at consequat tincidunt, turpis mauris mattis augue, sed commodo enim sem non sapien. In nisl ipsum, porttitor vel fringilla ac, dictum id urna.",
		style = TextStyle(
			color = Color(0x88FFFFFF),
			fontFamily = marcherFontFamily,
			fontSize = 16.sp
		),
		modifier = Modifier
			.padding(16.dp)
			.fillMaxWidth(),
	)
}

@Composable
fun ShowTags(show: Show)
{
	val tags : List<String> = listOf("Child", "Musical", "Drama")

	LazyRow(
		modifier = Modifier.padding(16.dp)
	) {
		items(tags.size) { index ->
			SuggestionChip(
				onClick = { },
				label = {
					Text(
						text = tags[index],
						style = TextStyle(
							color = Color.White,
							fontFamily = marcherFontFamily,
							fontSize = 14.sp
						),
						textAlign = TextAlign.Center,
						modifier = Modifier
							.padding(end = 8.dp)
					)
				},
			)
		}
	}
}


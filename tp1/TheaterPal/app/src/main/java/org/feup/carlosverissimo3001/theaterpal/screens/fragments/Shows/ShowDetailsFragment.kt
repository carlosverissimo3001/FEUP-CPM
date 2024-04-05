package org.feup.carlosverissimo3001.theaterpal.screens.fragments.Shows

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import org.feup.carlosverissimo3001.theaterpal.MyColors
import org.feup.carlosverissimo3001.theaterpal.marcherFontFamily
import org.feup.carlosverissimo3001.theaterpal.models.Show

@Composable
fun ShowDetails(ctx: Context, navController: NavController) {
	val show: Show? = navController.previousBackStackEntry?.savedStateHandle?.get("show")
	val bitmap: Bitmap? = navController.previousBackStackEntry?.savedStateHandle?.get("bitmap")
	if (show != null && bitmap != null)
		ShowDetailsScreen(ctx, show, bitmap, navController)

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ShowDetailsScreen(ctx: Context, show: Show, bitmap: Bitmap, navController: NavController) {
	Scaffold (
		floatingActionButton = {MyFloatingActionButton()},
		floatingActionButtonPosition = FabPosition.Center,
		topBar = {
			GoBackButton(navController)
		}
	) {

		LazyColumn {
			item {
				ShowImage(bitmap, navController)
			}
			item{
				ShowName(show)
			}
			item {
				ShowDescription(show)
			}
			item {
				Spacer(
					modifier = Modifier
						.size(48.dp)
				)
			}
		}
	}

}

@Composable
fun ShowImage(bitmap: Bitmap, navController: NavController) {
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
fun MyFloatingActionButton()
{
	ExtendedFloatingActionButton(
		text = {
			Text(
				text = "Buy Tickets",
				style = TextStyle(
					color = Color.White,
					fontFamily = marcherFontFamily,
				)
			)
		},
		icon = {
			Icon(
				imageVector = Icons.Filled.ConfirmationNumber,
				contentDescription = null,
				tint = Color.White
			)
		},
		containerColor = MyColors.tertiaryColor,
		shape = RoundedCornerShape(50.dp),
		onClick = { /*TODO*/ },
	)
}

@Composable
fun GoBackButton(navController : NavController){
	FilledIconButton(
		onClick = { navController.popBackStack() },
		shape = RoundedCornerShape(50.dp),
		colors = IconButtonColors(
			containerColor = Color(0x22FFFFFF),
			contentColor = Color.White,
			disabledContainerColor = Color.White,
			disabledContentColor = Color.White
		),
		modifier = Modifier.padding(16.dp)
	){
		Icon(
			imageVector = Icons.Filled.ArrowBackIosNew,
			contentDescription = null,
			tint = Color.White,
			modifier = Modifier.size(24.dp)
		)
	}
}


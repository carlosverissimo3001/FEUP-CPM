package org.feup.carlosverissimo3001.theaterpal.screens.fragments.shows

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.HorizontalRule
import androidx.compose.material3.Button
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import org.feup.carlosverissimo3001.theaterpal.api.purchaseTickets
import org.feup.carlosverissimo3001.theaterpal.marcherFontFamily
import org.feup.carlosverissimo3001.theaterpal.models.show.Show
import org.feup.carlosverissimo3001.theaterpal.models.show.ShowDate

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
	var showdateid by remember { mutableIntStateOf(-1) }
	var quantity by remember { mutableIntStateOf(0) }

	Scaffold (
		topBar = {
			GoBackButton(navController)
		}
	) {

		LazyColumn(
			horizontalAlignment = Alignment.CenterHorizontally,
		){
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
				ShowDatesDropdown(
					show,
					onDateSelected = { date : ShowDate ->
						showdateid = date.showdateid
					},
					onDecrement = { quantity -= 1 },
					onIncrement = { quantity += 1 }
				)
			}
			item{
				BuyButton (
					isVisible = (showdateid > -1 && quantity > 0),
				) {
					purchaseTickets(ctx, showdateid, quantity, quantity*show.price)
					navController.navigate("wallet")
				}
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
		text = "${show.releasedate} · ${show.duration} min · ${show.price}€",
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
		text = show.description,
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShowDatesDropdown(
    show: Show,
    onDateSelected: (ShowDate) -> Unit,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit
){
	val (quantity, setQuantity) = remember { mutableIntStateOf(0) }

	var isExpanded by remember {
		mutableStateOf(false)
	}

	var showDate by remember {
		mutableStateOf("")
	}

	val availableDates = show.dates.map { it.date }


	Row(
		horizontalArrangement = Arrangement.Start,
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier
			.padding(top = 16.dp, start = 32.dp, end = 16.dp, bottom = 16.dp)
	) {
		ExposedDropdownMenuBox(
			expanded = isExpanded,
			onExpandedChange = {newVal -> isExpanded = newVal},
		) {
			OutlinedTextField(
				value = showDate,
				shape = RoundedCornerShape(10.dp),
				onValueChange = {},
				textStyle = TextStyle(
					color = Color.White,
					fontSize = 16.sp,
					textAlign = TextAlign.Center,
					fontFamily = marcherFontFamily
				),
				readOnly = true,
				placeholder = {
					Text(
						text = "Select an available date",
						style = TextStyle(
							color = Color.White,
							fontSize = 16.sp,
							textAlign = TextAlign.Center,
							fontFamily = marcherFontFamily
						)
					)
				},
				colors = TextFieldDefaults.outlinedTextFieldColors(
					backgroundColor = Color(0x11FFFFFF),
					focusedBorderColor = Color.Transparent,
				),
				modifier = Modifier
					.fillMaxWidth(0.7f)
			)
			ExposedDropdownMenu(
				expanded = isExpanded,
				onDismissRequest = {
					isExpanded = false
				},
				modifier = Modifier
					.background(Color(0xFF444444))
			) {

				availableDates.forEach{ selectionOption  ->
					DropdownMenuItem(
						modifier = Modifier
							.background(Color(0xFF444444)),
						onClick = {
							val selected = show.dates.find { it.date == selectionOption }
							selected?.let {
								showDate = selectionOption
								onDateSelected(it)
								isExpanded = false
							}
						},
						text = {Text(
							text = selectionOption,
							color = if (showDate == selectionOption) MyColors.tertiaryColor else Color.White,
							fontSize = 14.sp,
							fontFamily = marcherFontFamily
						)},
						colors = MenuDefaults.itemColors(
							textColor = Color.Black,
						)
					)
				}
			}
		}

		// Decrement button
		IconButton(
			onClick = {
				if (quantity > 0) {
					setQuantity(quantity - 1)
					onDecrement()
				}
			},
		) {
			Icon(
				imageVector = Icons.Filled.HorizontalRule,
				contentDescription = null,
				tint = Color.White,
				modifier = Modifier.size(15.dp)
			)
		}

		// Quantity
		Text(
			text = quantity.toString(),
			style = TextStyle(
				fontFamily = marcherFontFamily,
				fontWeight = FontWeight.Bold,
				fontSize = 16.sp
			)
		)

		// Increment button
		IconButton(
			onClick = {
				if (quantity < 4) {
					setQuantity(quantity + 1)
					onIncrement()
				}
			},
		) {
			Icon(
				imageVector = Icons.Filled.Add,
				contentDescription = null,
				tint = Color.White,
				modifier = Modifier.size(18.dp)
			)
		}

	}
}

@Composable
fun BuyButton(
	isVisible: Boolean,
	onSubmit: () -> Unit)
{
	if (!isVisible)
		return
	Button(
		colors = ButtonDefaults.buttonColors(
			MyColors.tertiaryColor,
		),
		modifier = Modifier
			.padding(16.dp)
			.fillMaxWidth(0.9f)
			.height(55.dp),
		shape = RoundedCornerShape(10.dp),
		onClick = { onSubmit() },
	)
	{
		Text(
			text = "Buy tickets",
			style = TextStyle(
				color = Color.White,
				fontFamily = marcherFontFamily,
				fontSize = 16.sp
			)
		)
	}
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


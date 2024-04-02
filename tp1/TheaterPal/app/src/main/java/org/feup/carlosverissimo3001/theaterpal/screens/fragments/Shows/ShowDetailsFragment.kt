package org.feup.carlosverissimo3001.theaterpal.screens.fragments.Shows

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import org.feup.carlosverissimo3001.theaterpal.models.Show

@Composable
fun ShowDetails(ctx: Context, navController: NavController) {
	val let: Show? = navController.previousBackStackEntry?.savedStateHandle?.get("show")
	if (let != null) {
		ShowDetailsScreen(ctx, let)
	}
}

@Composable
fun ShowDetailsScreen(ctx: Context, show: Show) {
	Box(
		contentAlignment = Alignment.Center,
		modifier = Modifier.fillMaxSize(),
	) {
		Column(
			modifier = Modifier
				.fillMaxWidth()
		) {
			Text(
				text = show.name,
				color = Color.White
			)
			Text(
				text = show.description,
				color = Color.White
			)
		}
	}
}
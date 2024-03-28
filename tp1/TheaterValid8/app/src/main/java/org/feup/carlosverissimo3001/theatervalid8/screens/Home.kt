package org.feup.carlosverissimo3001.theatervalid8.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import org.feup.carlosverissimo3001.theatervalid8.models.Show
import java.lang.reflect.Modifier

@Composable
fun ShowDropdown(shows: List<Show>, selectedShow: Show?, onShowSelected: (Show) -> Unit) {
    Column {
        shows.forEach { show ->
            Box {
                ShowItem(show, selectedShow, onShowSelected)
            }
        }
    }
}

@Composable
fun ShowItem(show: Show, selectedShow: Show?, onShowSelected: (Show) -> Unit) {
    Text(
        text = show.name,
        // style = MaterialTheme.typography.body1,
        // color = if (show == selectedShow) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface,
    )
}

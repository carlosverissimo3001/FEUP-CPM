package org.feup.carlosverissimo3001.theatervalid8

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.feup.carlosverissimo3001.theatervalid8.models.Show
import org.feup.carlosverissimo3001.theatervalid8.models.ShowDate
import org.feup.carlosverissimo3001.theatervalid8.screens.ShowDatesDropDownMenu
import org.feup.carlosverissimo3001.theatervalid8.screens.ShowDropdownMenu
import org.feup.carlosverissimo3001.theatervalid8.screens.ValidateButton


class MainActivity : AppCompatActivity() {
    private var shows: List<Show> = emptyList()
    private var apiLayer = APILayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Fetch the shows
        apiLayer.fetchShows { shows ->
            this.shows = shows

            runOnUiThread {
                setContent {
                    val (selectedShow, setSelectedShow) = remember { mutableStateOf<Show?>(null) }
                    val (availableDates, setAvailableDates) = remember { mutableStateOf<List<ShowDate>>(emptyList()) }
                    val (selectedDate, setSelectedDate) = remember { mutableStateOf<ShowDate?>(null) }

                    CenteredContent {
                        if (shows.isNotEmpty()) {
                              ShowDropdownMenu(
                                shows = shows,
                                selectedShow = selectedShow,
                                onShowSelected = {
                                    selectedDate?.let { setSelectedDate(null) }
                                    setSelectedShow(it)
                                    setAvailableDates(it.dates)
                                }
                            )

                            ShowDatesDropDownMenu(
                                selectedShow = selectedShow,
                                availableDates = availableDates,
                                selectedShowDate = selectedDate,
                                onShowDateSelected = {
                                    setSelectedDate(it)
                                }
                            )
                            ValidateButton(
                                selectedShow = selectedShow,
                                selectedShowDate = selectedDate,
                                onClick = {
                                    println("Entering validation screen")
                                    println("Selected show: ${selectedShow?.name}")
                                    println("Selected date: ${selectedDate?.date}")
                                }
                            )
                        } else {
                            println("Loading shows...")
                            LoadingSpinner()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CenteredContent(content: @Composable () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        content()
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



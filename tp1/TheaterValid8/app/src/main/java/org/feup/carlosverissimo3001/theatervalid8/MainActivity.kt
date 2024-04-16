package org.feup.carlosverissimo3001.theatervalid8

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.feup.carlosverissimo3001.theatervalid8.api.APILayer
import org.feup.carlosverissimo3001.theatervalid8.models.show.*
import org.feup.carlosverissimo3001.theatervalid8.screens.ShowDatesDropDownMenu
import org.feup.carlosverissimo3001.theatervalid8.screens.ShowDropdownMenu
import org.feup.carlosverissimo3001.theatervalid8.screens.ValidateButton


class MainActivity : AppCompatActivity() {
    private var shows: List<Show> = emptyList()
    private var apiLayer = APILayer(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val supportToolAc = supportActionBar
        supportToolAc?.title = "TheaterValid8"

        setContent {
            CenteredContent {
                Topbar()
                LoadingSpinner()
            }
        }

        // Fetch the shows
        apiLayer.fetchShows { shows ->
            this.shows = shows

            runOnUiThread {
                setContent {
                    val (selectedShow, setSelectedShow) = remember { mutableStateOf<Show?>(null) }
                    val (availableDates, setAvailableDates) = remember { mutableStateOf<List<ShowDate>>(emptyList()) }
                    val (selectedDate, setSelectedDate) = remember { mutableStateOf<ShowDate?>(null) }

                    Topbar()

                    CenteredContent {
                        if (shows.isNotEmpty()) {
                            ShowDropdownMenu(
                                shows = shows,
                                selectedShow = selectedShow,
                                onShowSelected = {
                                    if (selectedShow != it) {
                                        selectedDate?.let { setSelectedDate(null) }
                                        setSelectedShow(it)
                                        setAvailableDates(it.dates)
                                    }
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
                                    intent = Intent(this, ValidatorActivity::class.java)
                                    intent.putExtra("show", selectedShow)
                                    intent.putExtra("showDate", selectedDate)
                                    startActivity(intent)
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
fun LoadingSpinner() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            color = Color.White,
            strokeWidth = 2.dp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Topbar() {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
        rememberTopAppBarState()
    )

    CenterAlignedTopAppBar(
        title = {
            Text("TheaterValid8",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.W700
                )
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        scrollBehavior = scrollBehavior,
    )
}

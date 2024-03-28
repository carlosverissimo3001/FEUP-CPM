package org.feup.carlosverissimo3001.theatervalid8

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import androidx.compose.runtime.Composable
import org.feup.carlosverissimo3001.theatervalid8.models.Show
import org.feup.carlosverissimo3001.theatervalid8.screens.ShowDropdown


class MainActivity : AppCompatActivity() {
    private var shows: List<Show> = emptyList()

    private var apiLayer = APILayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        // Fetch the shows
        apiLayer.fetchShows { shows ->
            this.shows = shows

            runOnUiThread {
                setContent {
                    ShowDropdown(shows = shows, selectedShow = null) { selectedShow ->
                        // Handle the selection of the show
                        // For example, you can update a variable with the selected show
                        // Or perform any other action based on the selected show
                        println("Selected show: $selectedShow")
                    }
                }
            }
        }
    }
}


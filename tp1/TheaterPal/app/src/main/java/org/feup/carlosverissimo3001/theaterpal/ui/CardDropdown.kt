package org.feup.carlosverissimo3001.theaterpal.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CardDropdown() {
    // Example data for month and year
    val months = (1..12).map { it.toString().padStart(2, '0') }
    val years = (2024..2044).map { it.toString() }

    // MutableState for selected month and year
    var selectedMonth by remember {
        mutableStateOf(months[0])
    }

    var selectedYear by remember {
        mutableStateOf(years[0])
    }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = selectedMonth,
            onValueChange = {selectedMonth = it },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = selectedYear,
            onValueChange = {selectedYear = it },
            modifier = Modifier.fillMaxWidth()
        )

        // Dropdown menus for month and year
        DropdownMenu(
            expanded = false, // Set to true when clicked
            onDismissRequest = { /* Handle dismiss */ }
        ) {
            months.forEach { month ->
                DropdownMenuItem(onClick = { selectedMonth = month }) {
                    // Customize dropdown item appearance
                }
            }
        }

        DropdownMenu(
            expanded = false, // Set to true when clicked
            onDismissRequest = { /* Handle dismiss */ }
        ) {
            years.forEach { year ->
                DropdownMenuItem(onClick = { selectedYear = year }) {
                    // Customize dropdown item appearance
                }
            }
        }
    }
}
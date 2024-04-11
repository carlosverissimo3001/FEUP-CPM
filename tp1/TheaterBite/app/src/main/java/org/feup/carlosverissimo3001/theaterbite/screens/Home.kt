package org.feup.carlosverissimo3001.theaterbite.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.feup.carlosverissimo3001.theaterbite.models.Show
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.feup.carlosverissimo3001.theaterbite.models.ShowDate
import org.feup.carlosverissimo3001.theaterbite.poppinsFontFamily


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShowDropdownMenu(shows: List<Show> = emptyList(), selectedShow: Show?, onShowSelected: (Show) -> Unit){
    val options = shows.map { it.name }

    var isExpanded by remember {
        mutableStateOf(false)
    }
    var show by remember {
        mutableStateOf("")
    }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = {newVal -> isExpanded = newVal},
    ) {
        OutlinedTextField(
            value = show,
            onValueChange = {},
            textStyle = TextStyle(
                color = MaterialTheme.colors.onSurface,
                fontSize = MaterialTheme.typography.h6.fontSize,
                textAlign = TextAlign.Center,
                fontFamily = poppinsFontFamily,
            ),
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            placeholder = {
                Text(
                    text = "Please select the show to validate",
                    style = TextStyle (
                        color = MaterialTheme.colors.onSurface,
                        fontSize = MaterialTheme.typography.body1.fontSize,
                        textAlign = TextAlign.Center,
                        fontFamily = poppinsFontFamily
                    )
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = (if (isSystemInDarkTheme())
                    lightColorScheme().background else darkColorScheme().background),
            ),
            modifier = Modifier.fillMaxWidth(fraction = 0.9f)
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = {
                isExpanded = false
            }
        ) {

            options.forEach{ selectionOption  ->
                DropdownMenuItem(onClick = {
                    val selected = shows.find { it.name == selectionOption }
                    selected?.let {
                        show = selectionOption
                        onShowSelected(it)
                        isExpanded = false
                    }
                }) {
                    Text(
                        text = selectionOption,
                        fontSize = MaterialTheme.typography.subtitle1.fontSize,
                        fontWeight = if (selectedShow?.name == selectionOption) FontWeight.Black else FontWeight.Light,
                        fontFamily = poppinsFontFamily
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShowDatesDropDownMenu(
	selectedShow: Show?,
	availableDates: List<ShowDate>,
	selectedShowDate: ShowDate?,
	onShowDateSelected: (ShowDate) -> Unit
){
    val options = availableDates.map {it.date}

    var isExpanded by remember {
        mutableStateOf(false)
    }

    var showDate by remember {
        mutableStateOf("")
    }

    if (selectedShow != null && showDate != "") {
        // Show changed, reset selected date
        if (!isDateInArray(showDate, selectedShow.dates)) {
            println("Old date: $showDate")
            showDate = ""
        }
    }


    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = {newVal -> isExpanded = newVal},
    ) {
        OutlinedTextField(
            value = showDate,
            onValueChange = {},
            textStyle = TextStyle(
                color = MaterialTheme.colors.onSurface,
                fontSize = MaterialTheme.typography.body1.fontSize,
                textAlign = TextAlign.Center,
                fontFamily = poppinsFontFamily
            ),
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            placeholder = {
                Text(
                    text = (if (availableDates.isEmpty()) "No show is selected"
                    else "Please select the show date to validate"),
                    style = TextStyle(
                        color = MaterialTheme.colors.onSurface,
                        fontSize = MaterialTheme.typography.body2.fontSize,
                        textAlign = TextAlign.Center,
                        fontFamily = poppinsFontFamily
                    )
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = (if (isSystemInDarkTheme())
                    lightColorScheme().background else darkColorScheme().background),
            ),
            modifier = Modifier.padding(top = 20.dp).fillMaxWidth(fraction = 0.9f),
            enabled = availableDates.isNotEmpty()
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = {
                isExpanded = false
            }
        ) {

            options.forEach{ selectionOption  ->
                DropdownMenuItem(onClick = {
                    val selected = availableDates.find { it.date == selectionOption }
                    selected?.let {
                        showDate = selectionOption
                        onShowDateSelected(it)
                        isExpanded = false
                    }
                }) {
                    Text(
                        text = selectionOption,
                        fontSize = MaterialTheme.typography.subtitle1.fontSize,
                        fontWeight = if (selectedShowDate?.date == selectionOption) FontWeight.Black else FontWeight.Light,
                        fontFamily = poppinsFontFamily
                    )
                }
            }
        }
    }
}

@Composable
fun ValidateButton(
	selectedShowDate: ShowDate?,
	selectedShow: Show?,
	onClick: () -> Unit,
) {
    Button(
        onClick = {
            onClick()
        },
        enabled = selectedShowDate != null && selectedShow != null,
        modifier = Modifier
            .padding(top = 40.dp)
            .padding(horizontal = 16.dp),
        colors = ButtonDefaults.buttonColors( // Custom colors
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary
        ),
        shape = MaterialTheme.shapes.large, // Add rounded corners
    ) {
        Text(
            "Start Validation",
            style = TextStyle(
                color = MaterialTheme.colors.onPrimary,
                fontSize = MaterialTheme.typography.h6.fontSize,
                fontFamily = poppinsFontFamily
            )
        )
    }
}

// AUXILIARY FUNCTIONS

fun isDateInArray(date: String, dates: List<ShowDate>): Boolean {
    for (d in dates) {
        if (d.date == date) {
            return true
        }
    }
    return false
}

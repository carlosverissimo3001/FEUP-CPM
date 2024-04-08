package org.feup.carlosverissimo3001.theaterpal.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

data class CafeteriaItem(
    val name: String,
    val description: String,
    val price: Double,
    val icon: ImageVector = Icons.Outlined.Fastfood,
    val quantity: Int = 0
)

var barItems = listOf(
    "Popcorn",
    "Soda",
    "Coffee",
    "Sandwich",
    "Beer"
)

fun getCafeteriaItems(): List<CafeteriaItem> {
    return listOf(
        CafeteriaItem(
            name = "Popcorn",
            description = "Freshly popped popcorn",
            price = 3.00,
            icon = Icons.Outlined.Fastfood
        ),
        CafeteriaItem(
            name = "Soda",
            description = "Refreshing soda",
            price = 2.00,
            icon = Icons.Outlined.Fastfood
        ),
        CafeteriaItem(
            name = "Coffee",
            description = "Hot coffee",
            price = 1.00,
            icon = Icons.Outlined.Coffee
        ),
        CafeteriaItem(
            name = "Sandwich",
            description = "Delicious sandwich",
            price = 3.50,
            icon = Icons.Outlined.Fastfood
        )
    )
}
package org.feup.carlosverissimo3001.theatervalid8.models

data class Show(
    val showId: Int,
    val name: String,
    val description: String,
    val picture: String,
    val pictureBase64: String,
    val price: Int,
    val dates: List<ShowDate>
)

data class ShowDate(
    val date: String,
    val availableSeats: Int
)
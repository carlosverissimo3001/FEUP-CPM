package org.feup.carlosverissimo3001.theaterpal.models.show

/**
 * Data class representing a show
 * @property showId id of the show
 * @property name name of the show
 * @property description description of the show
 * @property picture picture of the show
 * @property pictureBase64 picture of the show in base64
 * @property releasedate release date of the show
 * @property duration duration of the show
 * @property price price of the show
 * @property dates list of show dates
 */
data class Show(
	val showId: Int,
	val name: String,
	val description: String,
	val picture: String,
	val pictureBase64: String,
	val releasedate: String,
	val duration: Int,
	val price: Int,
	val dates: List<ShowDate>
)


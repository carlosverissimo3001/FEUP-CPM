package org.feup.carlosverissimo3001.theaterpal.models.show

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Data class representing a show date
 * @property date date of the show
 * @property showdateid id of the show date
 */
@Parcelize
data class ShowDate(
    val date: String,
    val showdateid: Int
) : Parcelable
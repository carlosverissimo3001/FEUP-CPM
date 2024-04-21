package org.feup.carlosverissimo3001.theaterpal.models.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CafeteriaItem(
    val name: String,
    val description: String = "",
    val price: Double,
    val quantity: Int = 0
) : Parcelable





package org.feup.carlosverissimo3001.theaterpal.models.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Data class representing a bar order
 * @property items map of items in the order and their quantities
 * @property total total price of the order
 * @see CafeteriaItem
 */
@Parcelize
data class BarOrder(
    var items: Map<CafeteriaItem, Int>,
    var total: Double
) : Parcelable

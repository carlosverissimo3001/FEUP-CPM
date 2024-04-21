package org.feup.carlosverissimo3001.theaterbite.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Data class representing a product
 * @property name name of the product
 * @property price price of the product
 * @property quantity quantity of the product
 */
@Parcelize
data class Product(
    val name: String,
    val price: Double,
    val quantity: Int
) : Parcelable
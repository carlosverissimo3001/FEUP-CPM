package org.feup.carlosverissimo3001.theaterbite.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Data class representing an order
 * @property products list of products in the order
 * @property total total price of the order
 * @property vouchersUsed list of vouchers (ids) used in the order
 * @property orderNo order number
 */
@Parcelize
data class Order(
    val products: List<Product>,
    val total: Double,
    val vouchersUsed: List<String>,
    var orderNo: Int = 0
) : Parcelable
package org.feup.carlosverissimo3001.theaterbite.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Data class representing a confirmed order
 * @property orderNo order number
 * @property products list of products in the order
 * @property total total price of the order
 * @property vouchersUsed list of vouchers used in the order
 * @property vouchersGenerated list of vouchers generated in the order
 */
@Parcelize
data class ConfirmedOrder (
    val orderNo: Int,
    val products: List<Product>,
    val total: Double,
    val vouchersUsed: List<Voucher>,
    val vouchersGenerated: List<Voucher>,
) : Parcelable
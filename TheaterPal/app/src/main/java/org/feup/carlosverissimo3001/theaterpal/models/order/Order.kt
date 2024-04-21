package org.feup.carlosverissimo3001.theaterpal.models.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.feup.carlosverissimo3001.theaterpal.models.Voucher

/**
 * Data class representing an order
 * @property vouchersUsed list of vouchers used in the order
 * @property barOrder bar order (items, quantities and total)
 */
@Parcelize
data class Order(
    var vouchersUsed : List<Voucher>,
    var barOrder: BarOrder
) : Parcelable
package org.feup.carlosverissimo3001.theaterpal.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Data class representing a voucher
 * @property voucherid id of the voucher
 * @property voucherType type of the voucher
 * @property isUsed if the voucher is used
 * @property userid id of the user that owns the voucher
 */
@Parcelize
data class Voucher (
    val voucherid: String,
    val voucherType: String,
    val isUsed: Boolean,
    val userid: String,
) : Parcelable {
    /**
     * Converts a Voucher object into a JSON string
     * @return JSON string
     * @see Voucher
     */
    fun toJson(): String {
        return """
        {
            "voucherid": "$voucherid",
            "vouchertype": "$voucherType",
            "isUsed": $isUsed,
            "userid": "$userid"
        }
    """.trimIndent()
    }
}
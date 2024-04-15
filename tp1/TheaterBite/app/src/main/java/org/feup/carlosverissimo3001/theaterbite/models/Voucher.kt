package org.feup.carlosverissimo3001.theaterbite.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Voucher (
    val voucherid: String,
    val voucherType: String,
    val isUsed: Boolean,
    val userid: String,
) : Parcelable
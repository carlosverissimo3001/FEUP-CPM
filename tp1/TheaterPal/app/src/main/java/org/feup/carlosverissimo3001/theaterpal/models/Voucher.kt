package org.feup.carlosverissimo3001.theaterpal.models

import org.json.JSONObject

data class Voucher (
    val voucherid: String,
    val voucherType: String,
    val isUsed: Boolean,
    val userid: String,
) {
    fun getVoucherInfo(): String {
        return "Voucher of type $voucherType"
    }
}

fun parseVoucher (jsonObject: JSONObject): Voucher {
    return Voucher(
        jsonObject.getString("voucherid"),
        jsonObject.getString("vouchertype"),
        jsonObject.getBoolean("isUsed"),
        jsonObject.getString("user_id"),
    )
}

fun parseVoucherType (type: String) : String {
    return when (type) {
        "FREE_COFFEE" -> "Free Coffee"
        "FREE_POPCORN" -> "Free Popcorn"
        else -> "5% Discount"
    }
}
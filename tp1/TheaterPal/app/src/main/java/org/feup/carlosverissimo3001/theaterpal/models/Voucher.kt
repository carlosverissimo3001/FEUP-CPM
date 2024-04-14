package org.feup.carlosverissimo3001.theaterpal.models

import android.os.Parcelable
import org.json.JSONObject

data class Voucher (
    val voucherid: String,
    val voucherType: String,
    val isUsed: Boolean,
    val userid: String,
) : Parcelable {
    constructor(parcel: android.os.Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: android.os.Parcel, flags: Int) {
        parcel.writeString(voucherid)
        parcel.writeString(voucherType)
        parcel.writeByte(if (isUsed) 1 else 0)
        parcel.writeString(userid)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Voucher> {
        override fun createFromParcel(parcel: android.os.Parcel): Voucher {
            return Voucher(parcel)
        }

        override fun newArray(size: Int): Array<Voucher?> {
            return arrayOfNulls(size)
        }
    }
}

fun parseVoucher (jsonObject: JSONObject): Voucher {
    return Voucher(
        jsonObject.getString("voucherid"),
        jsonObject.getString("vouchertype"),
        jsonObject.getBoolean("isUsed"),
        jsonObject.getString("userid"),
    )
}

fun parseVoucherType (type: String) : String {
    return when (type) {
        "FREE_COFFEE" -> "Free Coffee"
        "FREE_POPCORN" -> "Free Popcorn"
        else -> "5% Discount"
    }
}
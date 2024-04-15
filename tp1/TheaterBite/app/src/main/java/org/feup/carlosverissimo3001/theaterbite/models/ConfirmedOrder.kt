package org.feup.carlosverissimo3001.theaterbite.models

import android.os.Parcelable
import org.json.JSONObject


data class ConfirmedOrder (
    val orderNo: Int,
    val products: List<Product>,
    val total: Double,
    val vouchersUsed: List<Voucher>,
    val vouchersGenerated: List<Voucher>,
) : Parcelable {
    constructor(parcel: android.os.Parcel) : this(
        parcel.readInt(),
        parcel.createTypedArrayList(Product)!!,
        parcel.readDouble(),
        parcel.createTypedArrayList(Voucher)!!,
        parcel.createTypedArrayList(Voucher)!!
    )

    override fun writeToParcel(parcel: android.os.Parcel, flags: Int) {
        parcel.writeInt(orderNo)
        parcel.writeTypedList(products)
        parcel.writeDouble(total)
        parcel.writeTypedList(vouchersUsed)
        parcel.writeTypedList(vouchersGenerated)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ConfirmedOrder> {
        override fun createFromParcel(parcel: android.os.Parcel): ConfirmedOrder {
            return ConfirmedOrder(parcel)
        }

        override fun newArray(size: Int): Array<ConfirmedOrder?> {
            return arrayOfNulls(size)
        }
    }
}

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
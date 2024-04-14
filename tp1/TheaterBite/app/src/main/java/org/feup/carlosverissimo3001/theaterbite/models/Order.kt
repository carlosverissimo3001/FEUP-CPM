package org.feup.carlosverissimo3001.theaterbite.models

import android.os.Parcel
import android.os.Parcelable

data class Product(
    val name: String,
    val price: Double,
    val quantity: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeDouble(price)
        parcel.writeInt(quantity)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}

data class Order(
    val products: List<Product>,
    val total: Double,
    val vouchersUsed: List<String>,
    var orderNo: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.createTypedArrayList(Product)!!,
        parcel.readDouble(),
        parcel.createStringArrayList()!!,
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(products)
        parcel.writeDouble(total)
        parcel.writeStringList(vouchersUsed)
        parcel.writeInt(orderNo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Order> {
        override fun createFromParcel(parcel: Parcel): Order {
            return Order(parcel)
        }

        override fun newArray(size: Int): Array<Order?> {
            return arrayOfNulls(size)
        }
    }
}
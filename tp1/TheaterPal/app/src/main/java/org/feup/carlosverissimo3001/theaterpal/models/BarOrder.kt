package org.feup.carlosverissimo3001.theaterpal.models

import android.os.Parcel
import android.os.Parcelable

data class BarOrder(
    var items: Map<CafeteriaItem, Int>,
    var total: Double
) : Parcelable {
    constructor(parcel: Parcel) : this(
        readMap(parcel),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        writeMap(parcel, items)
        parcel.writeDouble(total)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BarOrder> {
        override fun createFromParcel(parcel: Parcel): BarOrder {
            return BarOrder(parcel)
        }

        override fun newArray(size: Int): Array<BarOrder?> {
            return arrayOfNulls(size)
        }

        private fun readMap(parcel: Parcel): Map<CafeteriaItem, Int> {
            val size = parcel.readInt()
            val map = mutableMapOf<CafeteriaItem, Int>()
            repeat(size) {
                val key =
                    parcel.readParcelable<CafeteriaItem>(CafeteriaItem::class.java.classLoader)!!
                val value = parcel.readInt()
                map[key] = value
            }
            return map
        }

        private fun writeMap(parcel: Parcel, map: Map<CafeteriaItem, Int>) {
            parcel.writeInt(map.size)
            map.forEach { (key, value) ->
                parcel.writeParcelable(key, 0)
                parcel.writeInt(value)
            }
        }
    }
}

data class Order(
    var vouchersUsed : List<Voucher>,
    var barOrder: BarOrder
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.createTypedArrayList(Voucher)!!,
        parcel.readParcelable(BarOrder::class.java.classLoader)!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(vouchersUsed)
        parcel.writeParcelable(barOrder, flags)
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

fun printOrder(order: Order) {
    println("Order:")
    println("Vouchers used:")
    for (voucher in order.vouchersUsed) {
        println(voucher)
    }
    printBarOrder(order.barOrder)
}

fun printBarOrder(barOrder: BarOrder) {
    println("BarOrder:")
    for ((item, quantity) in barOrder.items) {
        println("$item: $quantity")
    }
    println("Total: ${barOrder.total}")
}

fun setTotal(barOrder: BarOrder, total: Double) {
    barOrder.total = total
}




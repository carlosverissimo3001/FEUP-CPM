package org.feup.carlosverissimo3001.theaterpal.models

import android.os.Parcelable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

data class CafeteriaItem(
    val name: String,
    val description: String,
    val price: Double,
    val quantity: Int = 0
) : Parcelable {
    constructor(parcel: android.os.Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: android.os.Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeDouble(price)
        parcel.writeInt(quantity)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CafeteriaItem> {
        override fun createFromParcel(parcel: android.os.Parcel): CafeteriaItem {
            return CafeteriaItem(parcel)
        }

        override fun newArray(size: Int): Array<CafeteriaItem?> {
            return arrayOfNulls(size)
        }
    }
}

var itemIcons = mapOf(
    "Popcorn" to Icons.Outlined.Fastfood,
    "Soda" to Icons.Outlined.Fastfood,
    "Coffee" to Icons.Outlined.Coffee,
    "Sandwich" to Icons.Outlined.Fastfood)

fun getCafeteriaItems(): List<CafeteriaItem> {
    return listOf(
        CafeteriaItem(
            name = "Popcorn",
            description = "Freshly popped popcorn",
            price = 3.00
        ),
        CafeteriaItem(
            name = "Soda",
            description = "Refreshing soda",
            price = 2.00
        ),
        CafeteriaItem(
            name = "Coffee",
            description = "Hot coffee",
            price = 1.00
        ),
        CafeteriaItem(
            name = "Sandwich",
            description = "Delicious sandwich",
            price = 3.50
        )
    )
}
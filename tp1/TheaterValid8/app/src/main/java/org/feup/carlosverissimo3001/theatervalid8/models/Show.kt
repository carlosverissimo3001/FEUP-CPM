package org.feup.carlosverissimo3001.theatervalid8.models

import android.os.Parcel
import android.os.Parcelable

data class Show(
    val showId: Int,
    val name: String,
    val description: String,
    val picture: String,
    val pictureBase64: String,
    val price: Int,
    val dates: List<ShowDate>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.createTypedArrayList(ShowDate)!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(showId)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(picture)
        parcel.writeString(pictureBase64)
        parcel.writeInt(price)
        parcel.writeTypedList(dates)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Show> {
        override fun createFromParcel(parcel: Parcel): Show {
            return Show(parcel)
        }

        override fun newArray(size: Int): Array<Show?> {
            return arrayOfNulls(size)
        }
    }
}



data class ShowDate(
    val date: String,
    val availableSeats: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(date)
        parcel.writeInt(availableSeats)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShowDate> {
        override fun createFromParcel(parcel: Parcel): ShowDate {
            return ShowDate(parcel)
        }

        override fun newArray(size: Int): Array<ShowDate?> {
            return arrayOfNulls(size)
        }
    }
}
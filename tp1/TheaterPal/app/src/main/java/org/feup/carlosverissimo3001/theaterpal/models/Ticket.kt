package org.feup.carlosverissimo3001.theaterpal.models

import android.os.Parcelable
import org.json.JSONObject

data class Ticket (
    val ticketid: String,
    val userid: String,
    val showName: String,
    val seat: String,
    val isUsed: Boolean,
    val date: String,
    val imagePath: String = "",
    var numTickets: Int = 1
) : Parcelable {
    constructor(parcel: android.os.Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt()
    )

    override fun writeToParcel(parcel: android.os.Parcel, flags: Int) {
        parcel.writeString(ticketid)
        parcel.writeString(userid)
        parcel.writeString(showName)
        parcel.writeString(seat)
        parcel.writeByte(if (isUsed) 1 else 0)
        parcel.writeString(date)
        parcel.writeString(imagePath)
        parcel.writeInt(numTickets)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Ticket> {
        override fun createFromParcel(parcel: android.os.Parcel): Ticket {
            return Ticket(parcel)
        }

        override fun newArray(size: Int): Array<Ticket?> {
            return arrayOfNulls(size)
        }
    }
}

fun parseTicket (jsonObject: JSONObject): Ticket {
    return Ticket(
        jsonObject.getString("ticketid"),
        jsonObject.getString("userid"),
        jsonObject.getString("showName"),
        jsonObject.getString("seat"),
        jsonObject.getBoolean("isUsed"),
        jsonObject.getString("date"),
        jsonObject.getString("imagePath"),
    )
}
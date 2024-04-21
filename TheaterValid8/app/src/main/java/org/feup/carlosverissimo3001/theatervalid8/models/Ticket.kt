package org.feup.carlosverissimo3001.theatervalid8.models

import android.os.Parcelable
import org.json.JSONObject

data class TicketState(
    val ticketid: String,
    val state: String
)

data class Ticket (
    val ticketid: String,
    val userid: String,
    val showName: String,
    val seat: String,
    var isUsed: Boolean,
    val date: String,
    var isValidated: Boolean = false,
    var stateDesc: String = ""
) : Parcelable {
    constructor(parcel: android.os.Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: android.os.Parcel, flags: Int) {
        parcel.writeString(ticketid)
        parcel.writeString(userid)
        parcel.writeString(showName)
        parcel.writeString(seat)
        parcel.writeByte(if (isUsed) 1 else 0)
        parcel.writeString(date)
        parcel.writeByte(if (isValidated) 1 else 0)
        parcel.writeString(stateDesc)
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
    )
}
package org.feup.carlosverissimo3001.theatervalid8.models

import org.json.JSONObject


data class Ticket (
    val ticketid: String,
    val userid: String,
    val showName: String,
    val seat: String,
    val isUsed: Boolean,
    val date: String,
    val imagePath: String = "",
)
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
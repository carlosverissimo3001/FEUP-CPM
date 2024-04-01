package org.feup.carlosverissimo3001.theaterpal.models

import org.json.JSONObject

data class Ticket (
    val ticketids: List<String>,
    val userid: String,
    val showName: String,
    val seat: String,
    val isUsed: Boolean,
    val date: String,
    val imagePath: String = "",
    var numTickets: Int = 1
) {
    fun getTicketInfo(): String {
        return "Ticket for show $showName, seat $seat, date $date"
    }
}

fun parseTicket (jsonObject: JSONObject): Ticket {
    return Ticket(
        List(1) { jsonObject.getString("ticketid") }, // List of 1 element
        jsonObject.getString("userid"),
        jsonObject.getString("showName"),
        jsonObject.getString("seat"),
        jsonObject.getBoolean("isUsed"),
        jsonObject.getString("date"),
        jsonObject.getString("imagePath"),
    )
}
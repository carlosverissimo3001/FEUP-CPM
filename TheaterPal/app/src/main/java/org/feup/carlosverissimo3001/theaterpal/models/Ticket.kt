package org.feup.carlosverissimo3001.theaterpal.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

/**
 * Data class representing a ticket
 * @property ticketid id of the ticket
 * @property userid id of the user that bought the ticket
 * @property showName name of the show
 * @property seat seat of the ticket
 * @property isUsed if the ticket was used
 * @property date date of the show
 * @property imagePath path to the image of the show the ticket is for
 */
@Parcelize
data class Ticket (
    val ticketid: String,
    val userid: String,
    val showName: String,
    val seat: String,
    var isUsed: Boolean,
    val date: String
) : Parcelable {

    /**
     * Converts a Ticket object into a JSON string
     * @return JSON string
     * @see Ticket
     */
    fun toJson(): String {
        return """
        {
            "ticketid": "$ticketid",
            "userid": "$userid",
            "showName": "$showName",
            "seat": "$seat",
            "isUsed": $isUsed,
            "date": "$date"
        }
    """.trimIndent()
    }
}

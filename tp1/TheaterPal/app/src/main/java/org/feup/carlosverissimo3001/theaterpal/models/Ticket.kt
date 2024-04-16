package org.feup.carlosverissimo3001.theaterpal.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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
    val isUsed: Boolean,
    val date: String,
    val imagePath: String = "",
) : Parcelable

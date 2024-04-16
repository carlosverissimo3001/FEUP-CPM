package org.feup.carlosverissimo3001.theaterpal.models.transaction

data class TransactionTicketItem(
    val date : String,
    val numtickets : Int,
    val showname : String,
    val price: Double = 0.00
)
package org.feup.carlosverissimo3001.theaterpal.models.transaction

data class TransactionCafeteriaItem(
    val itemname: String,
    val quantity: Int,
    val price: Double = 0.00
)

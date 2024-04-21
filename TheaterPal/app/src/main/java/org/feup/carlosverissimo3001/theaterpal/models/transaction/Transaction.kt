package org.feup.carlosverissimo3001.theaterpal.models.transaction

import org.feup.carlosverissimo3001.theaterpal.models.Voucher


data class Transaction(
    val transactionid: String,
    val transactiontype: String,
    var total: Double,
    var vouchersUsed: List<Voucher>,
    var vouchersGenerated: List<Voucher>,
    var items: List<Any>,
    var timestamp : String = ""
)








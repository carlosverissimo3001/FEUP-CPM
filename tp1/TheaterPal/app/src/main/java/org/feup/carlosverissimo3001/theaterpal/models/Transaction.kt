package org.feup.carlosverissimo3001.theaterpal.models

import org.json.JSONObject


data class Transaction(
    val transactionid: String,
    val transactiontype: String,
    var total: Double,
    var vouchersUsed: List<Voucher>,
    var items: List<Any>
)

data class CafeteriaTransactionItem(
    val itemname: String,
    val quantity: Int
)

data class TicketItem(
    val date : String,
    val numtickets : Int,
    val showname : String
)

fun parseTransaction(jsonObject: JSONObject): Transaction {
    val transactionid = jsonObject.getString("transaction_id")
    val transactiontype = jsonObject.getString("transaction_type")
    val total = jsonObject.getDouble("total")

    val vouchersUsed = mutableListOf<Voucher>()
    val vouchersUsedJsonArray = jsonObject.getJSONArray("vouchers_used")
    for (i in 0 until vouchersUsedJsonArray.length()) {
        val voucher = parseVoucher(vouchersUsedJsonArray.getJSONObject(i))
        vouchersUsed.add(voucher)
    }

    if (transactiontype == "CAFETERIA_ORDER") {
        val items = mutableListOf<CafeteriaTransactionItem>()
        val itemsJsonArray = jsonObject.getJSONArray("items")
        for (i in 0 until itemsJsonArray.length()) {
            val item = CafeteriaTransactionItem(
                itemsJsonArray.getJSONObject(i).getString("itemname"),
                itemsJsonArray.getJSONObject(i).getInt("quantity")
            )
            items.add(item)
        }
        return Transaction(transactionid, transactiontype, total, vouchersUsed, items)
    }

    else{
        val items = mutableListOf<TicketItem>()
        val itemsJsonArray = jsonObject.getJSONArray("items")
        for (i in 0 until itemsJsonArray.length()) {
            val item = TicketItem(
                itemsJsonArray.getJSONObject(i).getString("date"),
                itemsJsonArray.getJSONObject(i).getInt("num_tickets"),
                itemsJsonArray.getJSONObject(i).getString("show_name")
            )
            items.add(item)
        }
        return Transaction(transactionid, transactiontype, total, vouchersUsed, items)
    }
}


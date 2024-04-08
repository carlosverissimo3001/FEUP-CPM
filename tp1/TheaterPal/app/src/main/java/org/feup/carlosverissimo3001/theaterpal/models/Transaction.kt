package org.feup.carlosverissimo3001.theaterpal.models

import org.json.JSONObject


data class Transaction(
    val transactionid: String,
    val transactiontype: String,
    var total: Double,
    var vouchersUsed: List<Voucher>,
    var vouchersGenerated: List<Voucher>,
    var items: List<Any>,
    var timestamp : String = ""
)

data class CafeteriaTransactionItem(
    val itemname: String,
    val quantity: Int,
    val price: Double = 0.00
)

data class TicketItem(
    val date : String,
    val numtickets : Int,
    val showname : String,
    val price: Double = 0.00
)

fun createDefaultTransaction(): Transaction {
    return Transaction("", "", 0.0, emptyList(), emptyList(), emptyList())
}

fun parseTransaction(jsonObject: JSONObject): Transaction {
    val transactionid = jsonObject.getString("transaction_id")
    val transactiontype = jsonObject.getString("transaction_type")
    val total = jsonObject.getDouble("total")
    val timestamp = jsonObject.getString("timestamp")

    val vouchersUsed = mutableListOf<Voucher>()
    val vouchersUsedJsonArray = jsonObject.getJSONArray("vouchers_used")
    for (i in 0 until vouchersUsedJsonArray.length()) {
        val voucher = parseVoucher(vouchersUsedJsonArray.getJSONObject(i))
        vouchersUsed.add(voucher)
    }

    val  vouchersGenerated = mutableListOf<Voucher>()
    val vouchersGeneratedJsonArray = jsonObject.getJSONArray("vouchers_generated")
    for (i in 0 until vouchersGeneratedJsonArray.length()) {
        val voucher = parseVoucher(vouchersGeneratedJsonArray.getJSONObject(i))
        vouchersGenerated.add(voucher)
    }

    if (transactiontype == "CAFETERIA_ORDER") {
        val items = mutableListOf<CafeteriaTransactionItem>()
        val itemsJsonArray = jsonObject.getJSONArray("items")
        for (i in 0 until itemsJsonArray.length()) {
            val item = CafeteriaTransactionItem(
                itemsJsonArray.getJSONObject(i).getString("itemname"),
                itemsJsonArray.getJSONObject(i).getInt("quantity"),
                if (itemsJsonArray.getJSONObject(i).has("price"))
                    itemsJsonArray.getJSONObject(i).getDouble("price") else 0.00
            )
            items.add(item)
        }
        return Transaction(transactionid, transactiontype, total, vouchersUsed, vouchersGenerated, items, timestamp)
    }

    else{
        val items = mutableListOf<TicketItem>()
        val itemsJsonArray = jsonObject.getJSONArray("items")
        for (i in 0 until itemsJsonArray.length()) {
            val item = TicketItem(
                itemsJsonArray.getJSONObject(i).getString("date"),
                itemsJsonArray.getJSONObject(i).getInt("num_tickets"),
                itemsJsonArray.getJSONObject(i).getString("showName"),
                itemsJsonArray.getJSONObject(i).getInt("price").toDouble()
            )
            items.add(item)
        }
        return Transaction(transactionid, transactiontype, total, vouchersUsed, vouchersGenerated, items, timestamp)
    }
}


package org.feup.carlosverissimo3001.theaterpal.models

import org.feup.carlosverissimo3001.theaterpal.models.order.CafeteriaItem
import org.feup.carlosverissimo3001.theaterpal.models.order.OrderRcv
import org.feup.carlosverissimo3001.theaterpal.models.show.Show
import org.feup.carlosverissimo3001.theaterpal.models.show.ShowDate
import org.feup.carlosverissimo3001.theaterpal.models.transaction.Transaction
import org.feup.carlosverissimo3001.theaterpal.models.transaction.TransactionCafeteriaItem
import org.feup.carlosverissimo3001.theaterpal.models.transaction.TransactionTicketItem
import org.feup.carlosverissimo3001.theaterpal.toTitleCase
import org.json.JSONObject

object Parser {
    /**** ORDERS TAB ****/

    /**
     * Parses a JSON object into a CafeteriaItem object
     * @param JSONObject JSON object to parse
     * @return CafeteriaItem object
     * @see CafeteriaItem
     */
    private fun parseCafeteriaItem(json: JSONObject): CafeteriaItem {
        return CafeteriaItem(
            json.getString("itemname"),
            "",
            json.getDouble("price"),
            json.getInt("quantity")
        )
    }

    /**
     * Parses a JSON object into a OrderRcv object
     * @param JSONObject JSON object to parse
     * @return OrderRcv object
     * @see OrderRcv
     */
    fun parseOrderRcv(json: JSONObject): OrderRcv {
        val items = mutableListOf<CafeteriaItem>()
        val itemsJson = json.getJSONArray("items")
        for (i in 0 until itemsJson.length()) {
            items.add(parseCafeteriaItem(itemsJson.getJSONObject(i)))
        }

        val vouchers = json.getJSONArray("vouchers_used")

        return OrderRcv(
            items,
            json.getString("order_id"),
            json.getInt("order_number"),
            json.getDouble("total"),
            json.getString("transaction_id"),
            vouchers.length(),
            toTitleCase(json.getString("status").lowercase())
        )
    }

    /**** ORDERS TAB ****/

    /**** SHOWS TAB ****/

    /**
     * Parses a JSON object into a Show object
     * @param jsonObject JSON object to parse
     * @return Show object
     * @see Show
     */
    fun parseShow (jsonObject: JSONObject): Show {
        return Show(
            jsonObject.getInt("showid"),
            jsonObject.getString("name"),
            jsonObject.getString("description"),
            jsonObject.getString("picture"),
            jsonObject.getString("picture_b64"),
            jsonObject.getString("releasedate"),
            jsonObject.getInt("duration"),
            jsonObject.getInt("price"),
            jsonObject.getJSONArray("dates").let {
                List(it.length()) { i ->
                    parseShowDate(it.getJSONObject(i))
                }
            },
        )
    }

    /**
     * Parses a JSON object into a ShowDate object
     * @param jsonObject JSON object to parse
     * @return ShowDate object
     * @see ShowDate
     */
    private fun parseShowDate (jsonObject: JSONObject): ShowDate {
        return ShowDate (
            jsonObject.getString("date"),
            jsonObject.getInt("showdateid")
        )
    }

    /**
     * Converts a list of Show objects into a JSON string
     * @param shows List of Show objects to convert
     * @return JSON string
     * @see Show
     */
    fun showsToJson(shows: List<Show>) : String {
        val showsJson = StringBuilder()

        for (show in shows) {
            showsJson.append(showToJson(show))
            showsJson.append(",")
        }

        return """
        {
            "shows": [
                $showsJson
            ]
        }
    """.trimIndent()
    }

    /**
     * Converts a Show object into a JSON string
     * @param show Show object to convert
     * @return JSON string
     * @see Show
     */
    private fun showToJson (show: Show) : String {
        // note picture_b64 is not included
        // the image is cached when the shows are first fetched from the server
        // then, only the picture path is used to load the image
        return """
        {
            "showid": ${show.showId},
            "name": "${show.name}",
            "description": "${show.description}",
            "picture": "${show.picture}",
            "picture_b64": "",
            "releasedate": "${show.releasedate}",
            "duration": ${show.duration},
            "price": ${show.price},
            "dates": [
                ${show.dates.joinToString(",") { showDateToJson(it) }}
            ]
        }
    """.trimIndent()
    }

    /**
     * Converts a ShowDate object into a JSON string
     * @param showDate ShowDate object to convert
     * @return JSON string
     * @see ShowDate
     */
    private fun showDateToJson (showDate: ShowDate) : String {
        return """
        {
            "date": "${showDate.date}",
            "showdateid": ${showDate.showdateid}
        }
    """.trimIndent()
    }

    /**** SHOWS TAB ****/

    /**** TICKETS TAB ****/

    /**
     * Parses a JSON object into a Ticket object
     * @param jsonObject JSON object to parse
     * @return Ticket object
     * @see Ticket
     */
    fun parseTicket (jsonObject: JSONObject): Ticket {
        return Ticket(
            jsonObject.getString("ticketid"),
            jsonObject.getString("userid"),
            jsonObject.getString("showName"),
            jsonObject.getString("seat"),
            if (jsonObject.has("isUsed")) jsonObject.getBoolean("isUsed") else false,
            jsonObject.getString("date")
        )
    }

    /**
     * Converts a list of Ticket objects into a JSON string
     * @param tickets List of Ticket objects to convert
     * @return JSON string
     * @see Ticket
     */
    fun ticketsToJson(tickets: List<Ticket>): String {
        val ticketsJson = StringBuilder()

        for (ticket in tickets) {
            ticketsJson.append(ticket.toJson())
            ticketsJson.append(",")
        }

        return """
        [
            $ticketsJson
        ]
        
    """.trimIndent()
    }

    /**** TICKETS TAB ****/

    /**** TRANSACTIONS TAB ****/

    /**
     * Parses a JSON object into a TransactionTicketItem object
     * @param jsonObject JSON object to parse
     * @return TransactionTicketItem object
     * @see TransactionTicketItem
     */
    private fun parseTransactionTicketItem(jsonObject: JSONObject) : TransactionTicketItem {
        return TransactionTicketItem(
            jsonObject.getString("date"),
            jsonObject.getInt("num_tickets"),
            jsonObject.getString("showName"),
            jsonObject.getInt("price").toDouble()
        )
    }

    /**
     * Parses a JSON object into a TransactionCafeteriaItem object
     * @param jsonObject JSON object to parse
     * @return TransactionCafeteriaItem object
     * @see TransactionCafeteriaItem
     */
    private fun parseTransactionCafeItem(jsonObject: JSONObject) : TransactionCafeteriaItem {
        return TransactionCafeteriaItem(
            jsonObject.getString("itemname"),
            jsonObject.getInt("quantity"),
            if (jsonObject.has("price")) jsonObject.getDouble("price") else 0.00
        )
    }

    /**
     * Parses a JSON object into a Transaction object
     * @param jsonObject JSON object to parse
     * @return Transaction object
     * @see Transaction
     */
    fun parseTransaction(jsonObject: JSONObject): Transaction {
        val transactionid = jsonObject.getString("transaction_id")
        val transactiontype = jsonObject.getString("transaction_type")
        val total = jsonObject.getDouble("total")
        val timestamp = jsonObject.getString("timestamp")

        // Parse vouchers used
        val vouchersUsed = mutableListOf<Voucher>()
        val vouchersUsedJsonArray = jsonObject.getJSONArray("vouchers_used")
        for (i in 0 until vouchersUsedJsonArray.length()) {
            val voucher = parseVoucher(vouchersUsedJsonArray.getJSONObject(i))
            vouchersUsed.add(voucher)
        }

        // Parse vouchers generated
        val vouchersGenerated = mutableListOf<Voucher>()
        val vouchersGeneratedJsonArray = jsonObject.getJSONArray("vouchers_generated")
        for (i in 0 until vouchersGeneratedJsonArray.length()) {
            val voucher = parseVoucher(vouchersGeneratedJsonArray.getJSONObject(i))
            vouchersGenerated.add(voucher)
        }

        // Parses cafeteria order
        if (transactiontype == "CAFETERIA_ORDER") {
            val items = mutableListOf<TransactionCafeteriaItem>()
            val itemsJsonArray = jsonObject.getJSONArray("items")
            for (i in 0 until itemsJsonArray.length()) {
                val item = itemsJsonArray.getJSONObject(i)
                items.add(parseTransactionCafeItem(item))
            }
            return Transaction(transactionid, transactiontype, total, vouchersUsed, vouchersGenerated, items, timestamp)
        }

        // Parses ticket purchase
        else{
            val items = mutableListOf<TransactionTicketItem>()
            val itemsJsonArray = jsonObject.getJSONArray("items")
            for (i in 0 until itemsJsonArray.length()) {
                val ticket = itemsJsonArray.getJSONObject(i)
                items.add(parseTransactionTicketItem(ticket))
            }
            return Transaction(transactionid, transactiontype, total, vouchersUsed, vouchersGenerated, items, timestamp)
        }
    }

    /**** TRANSACTIONS TAB ****/

    /****** VOUCHERS TAB ****/

    /**
     * Parses a JSON object into a Voucher object
     * @param jsonObject JSON object to parse
     * @return Voucher object
     * @see Voucher
     */
    fun parseVoucher(jsonObject: JSONObject): Voucher {
        return Voucher(
            jsonObject.getString("voucherid"),
            jsonObject.getString("vouchertype"),
            jsonObject.getBoolean("isUsed"),
            jsonObject.getString("userid"),
        )
    }

    /**
     * Parse a voucher type into a human readable string
     * @param type Voucher type to parse
     * @return Human readable string
     * @see Voucher
     */
    fun parseVoucherType(type: String) : String {
        return when (type) {
            "FREE_COFFEE" -> "Free Coffee"
            "FREE_POPCORN" -> "Free Popcorn"
            else -> "5% Discount"
        }
    }

    /****** VOUCHERS TAB ****/


    /**** Other ****/

    /**
     * Parse a User object into a JSON string
     * @param user User object to parse
     * @return JSON string
     * @see User
     */
    fun userToJson(user: User): String {
        return """
        {
            "name": "${user.name}",
            "nif": "${user.nif}",
            "card": {
                "type": "${user.creditCard.type}",
                "number": "${user.creditCard.number}",
                "expiration_date" : "${user.creditCard.validity.month}/${user.creditCard.validity.year}"
            },
            "public_key": "${user.publicKey}"
        }
    """.trimIndent()
    }
}

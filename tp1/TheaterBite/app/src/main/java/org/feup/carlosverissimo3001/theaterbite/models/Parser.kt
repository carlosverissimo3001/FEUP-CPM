package org.feup.carlosverissimo3001.theaterbite.models

import org.json.JSONObject

/**
 * Class responsible for parsing JSON objects into model objects
 */
object Parser {
    /**
     * Parses a JSON object into a Product object
     * @param jsonObject JSON object to parse
     * @return Product object
     */
    fun parseProduct(jsonObject: JSONObject): Product {
        val name = jsonObject.optString("itemname", "")
        val price = jsonObject.optDouble("price", 0.0)
        val quantity = jsonObject.optInt("quantity", 0)
        return Product(name, price, quantity)
    }

    /**
     * Parses a JSON object into a Voucher object
     * @param jsonObject JSON object to parse
     * @return Voucher object
     */
    fun parseVoucher (jsonObject: JSONObject): Voucher {
        return Voucher(
            jsonObject.getString("voucherid"),
            jsonObject.getString("vouchertype"),
            jsonObject.getBoolean("isUsed"),
            jsonObject.getString("userid"),
        )
    }

    fun parseUser (jsonObject: JSONObject): User {
        return User(
            jsonObject.getString("name"),
            jsonObject.getString("nif"),
            jsonObject.getString("publicKey"),
            jsonObject.getString("userid")
        )
    }
}
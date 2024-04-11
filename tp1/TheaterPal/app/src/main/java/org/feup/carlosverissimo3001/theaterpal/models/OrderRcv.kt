package org.feup.carlosverissimo3001.theaterpal.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.toUpperCase
import org.feup.carlosverissimo3001.theaterpal.capitalized
import org.json.JSONObject

/*
{
    "items": [
    {
        "item_name": "Sandwich",
        "price": 3,
        "quantity": 2
    },
    {
        "item_name": "Coffee",
        "price": 3,
        "quantity": 1
    }
    ],
    "order_id": "b697b619-70e0-4a0d-b5a3-db409ae407a8",
    "order_number": 207,
    "total": 8,
    "transaction_id": "d441e94c-591f-45a2-b61c-b650304ed710",
    "vouchers": 0
},*/


data class OrderRcv(
    var items   : List<OrderRcvItem>,
    var order_id: String,
    var order_number: Int,
    var total   : Double,
    var transaction_id: String,
    var vouchers_used_cnt: Int,
    var status: String
)

data class OrderRcvItem(
    var item_name: String,
    var price    : Double,
    var quantity : Int
)

fun parseOrderRcvItem(json: JSONObject): OrderRcvItem {
    return OrderRcvItem(
        json.getString("itemname"),
        json.getDouble("price"),
        json.getInt("quantity")
    )
}

fun parseOrderRcv(json: JSONObject): OrderRcv {
    val items = mutableListOf<OrderRcvItem>()
    val itemsJson = json.getJSONArray("items")
    for (i in 0 until itemsJson.length()) {
        items.add(parseOrderRcvItem(itemsJson.getJSONObject(i)))
    }

    return OrderRcv(
        items,
        json.getString("order_id"),
        json.getInt("order_number"),
        json.getDouble("total"),
        json.getString("transaction_id"),
        json.getInt("vouchers"),
        json.getString("status").lowercase().capitalized()
    )
}

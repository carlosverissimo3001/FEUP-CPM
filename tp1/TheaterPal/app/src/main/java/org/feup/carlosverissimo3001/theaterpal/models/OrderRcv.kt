package org.feup.carlosverissimo3001.theaterpal.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

val states = arrayOf("Collected", "Preparing", "Ready", "Delivered")

data class OrderRcv(
    var items   : List<OrderRcvItem>,
    var order_id: String,
    var order_number: Int,
    var total   : Double,
    var transaction_id: String,
    var vouchers_used_cnt: Int,
    var state: String = "Preparing"
)

data class OrderRcvItem(
    var item_name: String,
    var price    : Double,
    var quantity : Int
)

fun parseOrderRcvItem(json: JSONObject): OrderRcvItem {
    return OrderRcvItem(
        json.getString("item_name"),
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
        states.random()
    )
}

fun orderRcvToJson(order: OrderRcv): JSONObject {
    val json = JSONObject()
    val items = mutableListOf<JSONObject>()
    for (item in order.items) {
        val itemJson = JSONObject()
        itemJson.put("item_name", item.item_name)
        itemJson.put("price", item.price)
        itemJson.put("quantity", item.quantity)
        items.add(itemJson)
    }
    json.put("items", items)
    json.put("order_id", order.order_id)
    json.put("order_number", order.order_number)
    json.put("total", order.total)
    json.put("transaction_id", order.transaction_id)
    json.put("vouchers", order.vouchers_used_cnt)
    return json
}

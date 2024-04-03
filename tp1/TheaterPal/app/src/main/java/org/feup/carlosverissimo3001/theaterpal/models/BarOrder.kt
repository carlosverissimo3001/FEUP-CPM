package org.feup.carlosverissimo3001.theaterpal.models

data class BarOrder(
    var items: Map<String, Int>,
    var total: Double
)

data class Order(
    var vouchersUsed : List<Voucher>,
    var barOrder: BarOrder
)

fun setTotal(barOrder: BarOrder, total: Double) {
    barOrder.total = total
}

fun printBarOrder(barOrder: BarOrder) {
    println("BarOrder:")
    for ((item, quantity) in barOrder.items) {
        println("$item: $quantity")
    }
    println("Total: ${barOrder.total}")
}

fun printOrder(order: Order) {
    println("Order:")
    println("Vouchers used:")
    for (voucher in order.vouchersUsed) {
        println(voucher)
    }
    printBarOrder(order.barOrder)
}

fun parseOrderToJson(order: Order): String {
    return "{\n" +
            "\"vouchersUsed\": [${order.vouchersUsed.joinToString(",") { it.voucherid.toString() }}],\n" +
            "\"barOrder\": {\n" +
            "\"items\": {${order.barOrder.items.entries.joinToString(",") { "\"${it.key}\": ${it.value}" }}},\n" +
            "\"total\": ${order.barOrder.total}\n" +
            "}\n" +
            "}"
}

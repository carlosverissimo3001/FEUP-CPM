package org.feup.carlosverissimo3001.theaterpal.models

data class BarOrder(
    var items: Map<CafeteriaItem, Int>,
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

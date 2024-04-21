package org.feup.carlosverissimo3001.theaterpal.models

import org.feup.carlosverissimo3001.theaterpal.models.order.BarOrder
import org.feup.carlosverissimo3001.theaterpal.models.order.CafeteriaItem
import org.feup.carlosverissimo3001.theaterpal.models.order.Order
import org.feup.carlosverissimo3001.theaterpal.models.transaction.Transaction

/**
 * Auxiliary object with helper functions for printing...
 */
object Auxiliary {
    /**
     * Mapping of cafeteria items to icons
     * @see CafeteriaItem
     */
    var itemEmojis = mapOf(
        "Popcorn" to "\uD83C\uDF7F",
        "Soda" to "\uD83E\uDD64",
        "Coffee" to "☕",
        "Sandwich" to "\uD83C\uDF54"
    )

    /**
     * Prints an order
     * @param order order to print
     * @see Order
     */
    fun printOrder(order: Order) {
        println("Order:")
        println("Vouchers used:")
        for (voucher in order.vouchersUsed) {
            println(voucher)
        }
        printBarOrder(order.barOrder)
    }

    /**
     * Prints a bar order
     * @param barOrder bar order to print
     * @see BarOrder
     */
    private fun printBarOrder(barOrder: BarOrder) {
        println("BarOrder:")
        for ((item, quantity) in barOrder.items) {
            println("$item: $quantity")
        }
        println("Total: ${barOrder.total}")
    }

    /**
     * Sets the total of a bar order
     * @param barOrder bar order to set the total
     * @param total total to set
     * @see BarOrder
     */
    fun setTotal(barOrder: BarOrder, total: Double) {
        barOrder.total = total
    }

    /**
     * Creates some cafeteria items
     * @return list of cafeteria items
     * @see CafeteriaItem
     */
    fun getCafeteriaItems(): List<CafeteriaItem> {
        return listOf(
            CafeteriaItem(
                name = "Popcorn",
                description = "Freshly popped popcorn",
                price = 3.00
            ),
            CafeteriaItem(
                name = "Soda",
                description = "Refreshing soda",
                price = 2.00
            ),
            CafeteriaItem(
                name = "Coffee",
                description = "Hot coffee",
                price = 1.00
            ),
            CafeteriaItem(
                name = "Sandwich",
                description = "Delicious sandwich",
                price = 3.50
            )
        )
    }

    /**
     * Creates a default transaction
     * @return default transaction
     * @see Transaction
     */
    fun createDefaultTransaction(): Transaction {
        return Transaction("", "", 0.0, emptyList(), emptyList(), emptyList())
    }
}
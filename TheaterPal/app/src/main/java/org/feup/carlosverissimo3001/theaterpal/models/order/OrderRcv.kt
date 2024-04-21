package org.feup.carlosverissimo3001.theaterpal.models.order

/**
 * Data class representing an order coming from the server
 * @property items list of items in the order
 * @property order_id id of the order
 * @property order_number number of the order
 * @property total total price of the order
 * @property transaction_id id of the transaction
 * @property vouchers_used_cnt number of vouchers used in the order
 * @property status status of the order
 * @see CafeteriaItem
 */
data class OrderRcv(
    var items   : List<CafeteriaItem>,
    var order_id: String,
    var order_number: Int,
    var total   : Double,
    var transaction_id: String,
    var vouchers_used_cnt: Int,
    var status: String
)



package org.feup.carlosverissimo3001.theaterbite.models

data class Product (
    val name: String,
    val price: Double,
    val quantity: Int
)

data class Order(
    val products: List<Product>,
    val total: Double,
    val vouchersUsed: List<String>
)
package org.feup.carlosverissimo3001.theaterpal.models

import org.feup.carlosverissimo3001.theaterpal.models.card.Card

/**
 * Data class representing a user
 * @property name name of the user
 * @property nif nif of the user
 * @property creditCard credit card of the user
 * @property publicKey public key of the user
 * @see Card
 */
data class User(
    val name: String,
    val nif: String,
    val creditCard: Card,
    var publicKey: String = ""
)

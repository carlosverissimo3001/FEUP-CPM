package org.feup.carlosverissimo3001.theaterpal.models.card

/**
 * Data class representing a card
 * @property type type of the card
 * @property number card number
 * @property validity card validity
 */
data class Card(
    var type: String,
    var number: String,
    var validity: CardValidity
)

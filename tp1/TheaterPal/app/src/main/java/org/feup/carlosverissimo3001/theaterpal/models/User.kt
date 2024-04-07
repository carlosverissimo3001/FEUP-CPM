package org.feup.carlosverissimo3001.theaterpal.models

enum class CardType {
    VISA,
    MASTERCARD,
    AMERICAN_EXPRESS
}

data class User(
    val name: String,
    val nif: String,
    val creditCard: Card,
    var publicKey: String = ""
)

data class Card(
    var type: CardType,
    var number: String,
    var validity: CardValidity
)

data class CardValidity(
    var month: Int,
    var year: Int
)

fun UserToJson(user: User): String {
    return """
        {
            "name": "${user.name}",
            "nif": "${user.nif}",
            "card": {
                "type": "${user.creditCard.type}",
                "number": "${user.creditCard.number}",
                "expiration_date" : "${user.creditCard.validity.month}/${user.creditCard.validity.year}"
            },
            "public_key": "${user.publicKey}"
        }
    """.trimIndent()
}
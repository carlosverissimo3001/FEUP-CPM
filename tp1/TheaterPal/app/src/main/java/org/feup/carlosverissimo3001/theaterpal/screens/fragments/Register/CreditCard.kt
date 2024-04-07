package org.feup.carlosverissimo3001.theaterpal.screens.fragments.Register


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.steliospapamichail.creditcardmasker.viewtransformations.CardNumberMask
import com.steliospapamichail.creditcardmasker.viewtransformations.ExpirationDateMask
import org.feup.carlosverissimo3001.theaterpal.marcherFontFamily
import org.feup.carlosverissimo3001.theaterpal.models.Card
import org.feup.carlosverissimo3001.theaterpal.models.CardType
import org.feup.carlosverissimo3001.theaterpal.models.CardValidity


@Composable
fun CreditCard(
    onCardChange: (Card) -> Unit
) {
    var cardNumber by remember { mutableStateOf("") }
    var cardValidity by remember { mutableStateOf("") }

    var defaultCard = Card(CardType.VISA, "", CardValidity(0, 0))

    var card by remember {
        mutableStateOf(defaultCard)
    }

    // TODO
    /*var cardType by remember {
        mutableStateOf<String>("")
    }*/
    var cardType = "Visa" // for now

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            CardNumber(onCardNumberChange = {
                cardNumber = it
                card.number = it

                if (it.length == 16)
                    onCardChange(card)
            })
            Spacer(modifier = Modifier.height(10.dp))
            Expiration(onExpirationChange = { exp ->
                cardValidity = exp

                if (isExpirationValid(exp)) {
                    card.validity = CardValidity(exp.substring(0, 2).toInt(), exp.substring(2, 4).toInt())
                    onCardChange(card)
                }
            })
            Spacer(modifier = Modifier.height(10.dp))
            /*TypeDropdown {
                cardType = it
                card.type = CardType.valueOf(it)
                onCardChange(card)
            }*/
        }
    }
}


@Composable
fun Expiration(
    onExpirationChange: (String) -> Unit,
) {
    var expiration by remember { mutableStateOf("") }
    OutlinedTextField(
        isError = !isExpirationError(expiration),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        leadingIcon = {
          Icon(
              imageVector = Icons.Default.DateRange,
                contentDescription = null
          )
        },
        value = expiration,
        visualTransformation = ExpirationDateMask(),
        onValueChange = {
            if (it.matches(Regex("[0-9/]*")))
                if (it.length <= 4){
                    expiration = it
                    onExpirationChange(it)
                }
        },
        label = {
            Text(
                "Expiration Date (MM/YY)",
                style = TextStyle(
                    color = Color.White,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    textAlign = TextAlign.Center,
                    fontFamily = marcherFontFamily
                )

            ) },
        colors = fieldColors(),
    )
}

@Composable
fun CardNumber(
    onCardNumberChange: (String) -> Unit
) {
    var number by remember { mutableStateOf("") }
    OutlinedTextField(
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        leadingIcon = {
            Icon(
            imageVector = Icons.Default.AddCard,
            contentDescription = null
        ) },
        value = number,
        visualTransformation = CardNumberMask(" "),
        onValueChange = {
            if (it.matches(Regex("[0-9/]*")))
                if (it.length <= 16) {
                    number = it
                    onCardNumberChange(it)
                }
        }, label = {
            Text(
                "Card Number",
                style = TextStyle(
                    color = Color.White,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    textAlign = TextAlign.Center,
                    fontFamily = marcherFontFamily
                )
            ) },
        colors = fieldColors(),
    )
}

@Composable
fun fieldColors() = OutlinedTextFieldDefaults.colors(
    unfocusedTextColor = Color.White,
    unfocusedBorderColor = Color.White,
    unfocusedLabelColor = Color.White,
    unfocusedLeadingIconColor = Color.White,
    focusedLeadingIconColor = Color.White,
    focusedLabelColor = Color.White,
    focusedBorderColor = Color.White,
    focusedTextColor = Color.White,

    errorLabelColor = Color.Red,
    errorLeadingIconColor = Color.Red,
    errorBorderColor = Color.Red,
    errorTextColor = Color.Red
)

fun isExpirationValid(expiration: String): Boolean {
    if (expiration.isEmpty() || expiration.length < 4)
        return false

    val month = expiration.substring(0, 2).toIntOrNull() ?: return false
    val year = expiration.substring(2, 4).toIntOrNull() ?: return false

    return month in 1..12 && year in 24..99
}

fun isExpirationError(expiration: String): Boolean {
    if (expiration.isEmpty() || expiration.length < 4)
        return true

    val month = expiration.substring(0, 2).toIntOrNull() ?: return false
    val year = expiration.substring(2, 4).toIntOrNull() ?: return false

    return month in 1..12 && year in 24..99
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypeDropdown(
    onTypeSelected: (String) -> Unit,
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    val types = CardType.entries.map { it.name }
    var type by remember { mutableStateOf("") }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it }
    ) {
        OutlinedTextField(
            value = type,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            placeholder = {
                Text(
                    text = "Please select the card type",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        textAlign = TextAlign.Center,
                        fontFamily = marcherFontFamily
                    )
                )
            }
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = {
                isExpanded = false
            }
        ) {
            println("Dropdown menu")
            types.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        val selected = types.find { it == selectionOption }
                        selected?.let {
                            type = selected
                            onTypeSelected(selected)
                            isExpanded = false
                        }
                    },
                    text = {
                        Text(
                            text = selectionOption,
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                textAlign = TextAlign.Center,
                                fontFamily = marcherFontFamily
                            )
                        )
                    }
                )
            }
        }
    }
}

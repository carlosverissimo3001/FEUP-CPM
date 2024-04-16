package org.feup.carlosverissimo3001.theaterpal.screens.fragments.register



import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.steliospapamichail.creditcardmasker.viewtransformations.CardNumberMask
import com.steliospapamichail.creditcardmasker.viewtransformations.ExpirationDateMask
import org.feup.carlosverissimo3001.theaterpal.marcherFontFamily
import org.feup.carlosverissimo3001.theaterpal.models.card.Card
import org.feup.carlosverissimo3001.theaterpal.models.card.CardValidity


@Composable
fun CreditCard(
    onCardChange: (Card) -> Unit,
    onError: (String) -> Unit
) {
    var cardNumber by remember { mutableStateOf("") }
    var cardValidity by remember { mutableStateOf("") }

    val defaultCard = Card("", "", CardValidity(0, 0))

    val card by remember {
        mutableStateOf(defaultCard)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            CardNumber(onCardNumberChange = {
                cardNumber = it
                card.number = it

                if (it.length == 16)
                    onCardChange(card)
            },
                onError = onError
            )
            Spacer(modifier = Modifier.height(10.dp))

            Expiration(onExpirationChange = { exp ->
                cardValidity = exp

                if (isExpirationValid(exp)) {
                    card.validity = CardValidity(exp.substring(0, 2).toInt(), exp.substring(2, 4).toInt())
                    onCardChange(card)
                }
            },
                onError = onError
            )

            Spacer(modifier = Modifier.height(10.dp))

            TypeDropdown (
                onTypeSelected = {
                    card.type = it
                    onCardChange(card)
                    println("Card : $card")
                }
            )
        }
    }
}


@Composable
fun Expiration(
    onExpirationChange: (String) -> Unit,
    onError: (String) -> Unit
) {
    var expiration by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    error = isExpirationError(expiration)
    onError(error)

    OutlinedTextField(
        isError = isExpirationError(expiration) != "",
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
    onCardNumberChange: (String) -> Unit,
    onError: (String) -> Unit
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
    errorTextColor = Color.Red,
    errorSupportingTextColor = Color.Red,
    errorPlaceholderColor = Color.Red,
    errorTrailingIconColor = Color.Red
)

fun isExpirationValid(expiration: String): Boolean {
    if (expiration.isEmpty() || expiration.length < 4)
        return false

    val month = expiration.substring(0, 2).toIntOrNull() ?: return false
    val year = expiration.substring(2, 4).toIntOrNull() ?: return false

    return month in 1..12 && year in 24..99
}

fun isExpirationError(expiration: String): String {
    if (expiration.isEmpty() || expiration.length < 4)
        return ""

    val month = expiration.substring(0, 2).toIntOrNull() ?: return "Please enter a valid month"
    val year = expiration.substring(2, 4).toIntOrNull() ?: return "Please enter a valid year"

    if (month !in 1..12 && year !in 24..99)
        return "Please enter a valid month and year"

    else if (month !in 1..12)
        return "Please enter a valid month"

    else if (year !in 24..99)
        return "Please enter a valid year"

    return ""
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypeDropdown(
    onTypeSelected: (String) -> Unit,
) {
    val types = arrayOf("VISA", "MASTERCARD", "AMERICAN EXPRESS", "DISCOVER")
    var expanded by remember { mutableStateOf(false) }
    var selectedCardType by remember { mutableStateOf("") }


    Box(
        contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            },
            modifier = Modifier.padding(10.dp)
                .border(
                    width = 1.dp,
                    color = Color.White,
                    shape = MaterialTheme.shapes.small
                )
        ) {
            OutlinedTextField(
                value = selectedCardType,
                onValueChange = {
                    selectedCardType = it
                },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                leadingIcon = @Composable {
                    Icon(
                        imageVector = Icons.Default.AddCard,
                        contentDescription = null,
                        tint = Color.White
                    )
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
                },
                modifier = Modifier.menuAnchor(),
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    fontFamily = marcherFontFamily
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {expanded = false}
            ) {
                types.forEach { type ->
                    DropdownMenuItem(
                        onClick = {
                            selectedCardType = type
                            expanded = false
                            onTypeSelected(type)
                        },
                        text = {
                            Text(
                                text = type,
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
}

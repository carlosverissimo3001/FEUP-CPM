package org.feup.carlosverissimo3001.theatervalid8.screens

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.feup.carlosverissimo3001.theatervalid8.R
import org.feup.carlosverissimo3001.theatervalid8.formatDate
import org.feup.carlosverissimo3001.theatervalid8.models.Ticket
import org.feup.carlosverissimo3001.theatervalid8.poppinsFontFamily
import org.feup.carlosverissimo3001.theatervalid8.screens.fragments.TicketValidate


@Suppress("DEPRECATION")
inline fun <reified T: Parcelable> Intent.getParcelableArrayListExtraProvider(identifierParameter: String): java.util.ArrayList<T>? {
    return if (Build.VERSION.SDK_INT >= 33) {
        this.getParcelableArrayListExtra(identifierParameter, T::class.java)
    } else {
        this.getParcelableArrayListExtra(identifierParameter)
    }
}

class ValidationStatusActivity : AppCompatActivity() {
    private lateinit var tickets: List<Ticket>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tickets = intent.getParcelableArrayListExtraProvider<Ticket>("tickets")!!
        val show = intent.getStringExtra("show")!!
        val date = intent.getStringExtra("date")!!

        setContent(
            content = {
                NfcSuccessFragment(
                    ctx = this,
                    onDone = {
                        finish()
                    },
                    tickets = tickets,
                    showName = show,
                    date = date
                )
            }
        )
    }
}

@Composable
fun NfcSuccessFragment(
    ctx: Context,
    onDone: () -> Unit,
    tickets : List<Ticket>,
    showName: String,
    date: String
) {
    val validatedTickets = tickets.filter { it.isValidated }
    val failedTickets = tickets.filter { !it.isValidated }

    Box(
        modifier = Modifier
            .background(
                Color(android.graphics.Color.parseColor("#302c2c")),
                RoundedCornerShape(15.dp)
            )
            .pointerInput(Unit) {
                // draw down == go back
                detectDragGestures { _, dragAmount ->
                    if (dragAmount.y > 50) {
                        onDone()
                    }
                }
            }
    ){
        Spacer(modifier = Modifier.size(10.dp))
        Column (
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ){
            Column (
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text = "Validation Status",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                    ),
                    color = Color.White,
                )
            }

            if (validatedTickets.isNotEmpty()) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    val text =
                        "The following ticket" + if (validatedTickets.size > 1) "s were" else " was"

                    Text(
                        "$text validated successfully!",
                        style = TextStyle(
                            fontFamily = poppinsFontFamily,
                            fontSize = MaterialTheme.typography.titleSmall.fontSize,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                        ),
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(1),
                        contentPadding = PaddingValues(10.dp),
                    ) {
                        items(validatedTickets.size) { index ->
                            TicketValidate(
                                ctx,
                                ticket = validatedTickets[index]
                            )
                        }
                    }
                }
            }

            if (failedTickets.isNotEmpty() && validatedTickets.isNotEmpty()) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    color = Color.White,
                    thickness = 3.dp
                )
            }

            if (failedTickets.isNotEmpty()) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val text =
                        "The following ticket" + if (failedTickets.size > 1) "s" else ""

                    Text(
                        "$text could not be validated!",
                        style = TextStyle(
                            fontFamily = poppinsFontFamily,
                            fontSize = MaterialTheme.typography.titleSmall.fontSize,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                        ),
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(1),
                        contentPadding = PaddingValues(10.dp),
                    ) {
                        items(failedTickets.size) { index ->
                            TicketValidate(
                                ctx,
                                ticket = failedTickets[index]
                            )
                        }
                    }

                }
            }



            Button(
                onClick = { onDone() },
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color (android.graphics.Color.parseColor("#50bc64")),
                    contentColor = androidx.compose.ui.graphics.Color.White
                )
            ) {
                Text(
                    "Scan more tickets",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}
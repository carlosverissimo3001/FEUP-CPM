package org.feup.carlosverissimo3001.theaterpal

import android.content.Intent
import androidx.compose.ui.graphics.Color
import android.graphics.Color.parseColor
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

class ServerDownActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BottomBarTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    ServerDownScreen(
                        onTryAgainClick = {
                            val intent = Intent(this, LauncherActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }

    @Composable
    fun ServerDownScreen(
        onTryAgainClick: () -> Unit
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ){
            Column(
                modifier = Modifier.align(Alignment.Center).fillMaxWidth(0.9f).background(
                    color = Color(parseColor("#36363e")),
                    shape = RoundedCornerShape(16.dp)
                ).padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Server Down",
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.error
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Oops! Something went wrong.",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        color = Color.White,
                        fontFamily = marcherFontFamily
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "The server is currently down.",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        color = Color.White,
                        fontFamily = marcherFontFamily
                    )
                )

                Button(
                    onClick = onTryAgainClick,
                    modifier = Modifier.padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Try Again",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            color = Color.White,
                            fontFamily = marcherFontFamily
                        )
                    )
                }
            }
        }

    }
}



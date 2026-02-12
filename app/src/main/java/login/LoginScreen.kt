package login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.oriol.finance.ui.theme.*

@Composable
fun LoginScreen(): Unit {
    var email by remember { mutableStateOf("") }
    var passwd by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text("FINANCE APP", fontSize = 35.sp, fontWeight = FontWeight.Bold, fontFamily = Montserrat, color = Color(0xFF34D399))
        Spacer(Modifier.height(40.dp))
        Spacer(Modifier.height(20.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", fontFamily = Montserrat, color = White) },
            modifier = Modifier.fillMaxWidth(0.8f).clip(RoundedCornerShape(12.dp)),
            textStyle = TextStyle(
                color = White,
                fontFamily = Montserrat
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AccentGreen,
                unfocusedBorderColor = AccentGreen.copy(alpha = 0.6f),
                cursorColor = AccentGreen,
                focusedLabelColor = AccentGreen,
                unfocusedLabelColor = AccentGreen.copy(alpha = 0.7f),
                focusedTextColor = White,
                unfocusedTextColor = White
            )
            ,
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(Modifier.height(20.dp))
        OutlinedTextField(
            value = passwd,
            onValueChange = { passwd = it },
            label = { Text("Password", fontFamily = Montserrat, color = White) },
            modifier = Modifier.fillMaxWidth(0.8f).clip(RoundedCornerShape(12.dp)),
            textStyle = TextStyle(
                color = White,
                fontFamily = Montserrat
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AccentGreen,
                unfocusedBorderColor = AccentGreen.copy(alpha = 0.6f),
                cursorColor = AccentGreen,
                focusedLabelColor = AccentGreen,
                unfocusedLabelColor = AccentGreen.copy(alpha = 0.7f),
                focusedTextColor = White,
                unfocusedTextColor = White
            )
            ,
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(Modifier.height(35.dp))
        ElevatedButton(
            onClick = { /* TODO */ },
            modifier = Modifier.fillMaxWidth(0.65f),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = AccentGreen,
                contentColor = White
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                "Login",
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }

}
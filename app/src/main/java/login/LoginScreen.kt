package login

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch // NECESARIO para lanzar la tarea en segundo plano
// IMPORTS DE SUPABASE V3
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import supabase.SUPABASE

// Asegúrate de tener tus colores y fuentes importados
import dev.oriol.finance.ui.theme.* @Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    // Variables de estado del formulario
    var email by remember { mutableStateOf("") }
    var passwd by remember { mutableStateOf("") }

    // Variable para saber si estamos cargando (para bloquear el botón)
    var isLoading by remember { mutableStateOf(false) }

    // Variable para controlar la visibilidad de la contraseña (Opcional, pero recomendado)
    var passwordVisible by remember { mutableStateOf(false) }

    // NECESARIO: Scope para lanzar corrutinas (peticiones de red)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current // Para mostrar Toasts si falla

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("FINANCE APP", fontSize = 35.sp, fontWeight = FontWeight.Bold, fontFamily = Montserrat, color = Color(0xFF34D399))

        Spacer(Modifier.height(60.dp))

        // CAMPO EMAIL
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", fontFamily = Montserrat, color = White) },
            modifier = Modifier.fillMaxWidth(0.8f).clip(RoundedCornerShape(12.dp)),
            textStyle = TextStyle(color = White, fontFamily = Montserrat),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AccentGreen,
                unfocusedBorderColor = AccentGreen.copy(alpha = 0.6f),
                cursorColor = AccentGreen,
                focusedLabelColor = AccentGreen,
                unfocusedLabelColor = AccentGreen.copy(alpha = 0.7f),
                focusedTextColor = White,
                unfocusedTextColor = White
            ),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(Modifier.height(20.dp))

        // CAMPO PASSWORD
        OutlinedTextField(
            value = passwd,
            onValueChange = { passwd = it },
            label = { Text("Password", fontFamily = Montserrat, color = White) },
            modifier = Modifier.fillMaxWidth(0.8f).clip(RoundedCornerShape(12.dp)),
            textStyle = TextStyle(color = White, fontFamily = Montserrat),
            // IMPORTANTE: Esto oculta los caracteres con puntitos
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AccentGreen,
                unfocusedBorderColor = AccentGreen.copy(alpha = 0.6f),
                cursorColor = AccentGreen,
                focusedLabelColor = AccentGreen,
                unfocusedLabelColor = AccentGreen.copy(alpha = 0.7f),
                focusedTextColor = White,
                unfocusedTextColor = White
            ),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(Modifier.height(35.dp))

        // BOTÓN LOGIN
        ElevatedButton(
            onClick = {
                // Lógica de Login corregida
                if (email.isNotEmpty() && passwd.isNotEmpty()) {
                    isLoading = true // Activamos el estado de carga

                    // Lanzamos la corrutina (segundo plano)
                    scope.launch {
                        try {
                            // SINTAXIS CORRECTA SUPABASE V3
                            SUPABASE.auth.signInWith(Email) {
                                this.email = email
                                this.password = passwd
                            }
                            // Si llega aquí, es que ha funcionado (si no, salta al catch)
                            println("Login correcto")
                            onLoginSuccess()

                        } catch (e: Exception) {
                            // Si falla (contraseña mal, sin internet, etc)
                            e.printStackTrace()
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                        } finally {
                            isLoading = false // Desactivamos carga pase lo que pase
                        }
                    }
                } else {
                    Toast.makeText(context, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth(0.65f),
            enabled = !isLoading, // Deshabilita el botón si está cargando
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = AccentGreen,
                contentColor = White,
                disabledContainerColor = Color.Gray
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = White, modifier = Modifier.size(24.dp))
            } else {
                Text(
                    "Login",
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        }

        Spacer(Modifier.height(35.dp))

        // Textos de registro (sin cambios funcionales)
        Text("Not registered?", color = White, fontFamily = Montserrat, fontSize = 14.sp)
        Text("Register Now!", color = AccentGreen, fontFamily = Montserrat, fontSize = 14.sp)
    }
}
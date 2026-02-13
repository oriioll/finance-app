package dev.oriol.finance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.input.key.Key.Companion.Home
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.oriol.finance.ui.theme.FinanceAppTheme
import login.LoginScreen
import home.Home
import io.github.jan.supabase.auth.auth
import supabase.SUPABASE

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    FinanceAppTheme {
        val NAV_CONTROLLER: NavHostController = rememberNavController()
        val session = SUPABASE.auth.currentSessionOrNull()
        val startDestination = if (session != null) "home" else "login"
        NavHost(
            navController = NAV_CONTROLLER,
            startDestination = startDestination
        ) {
            // RUTA LOGIN
            composable("login") {
                LoginScreen(
                    onLoginSuccess = {
                        // Navegar a Home y borrar el historial de Login para no poder volver atrás
                        NAV_CONTROLLER.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }
            // RUTA HOME
            composable("home") {
                Home(
                    onLogout = {
                        // Navegar a Login y borrar historial
                        NAV_CONTROLLER.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
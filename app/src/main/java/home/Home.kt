package home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.*

@Composable
fun Home(onLogout: () -> Unit): Unit {
    Column(modifier = Modifier.fillMaxSize()) {
        Button(onClick = onLogout) {
            Text("Cerrar Sesión (Logout)")
        }
    }
}
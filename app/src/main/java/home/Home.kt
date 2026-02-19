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

fun makeMovement(userId: String = "bd11d87c-7da1-4d80-9b51-90e72bc1d8f4", import: Double, category: String = "Varios", isExpense: Boolean, typeId: Int): Boolean{
    TODO()
}
package home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.*
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import model.Movement
import supabase.SUPABASE



@Composable
fun Home(onLogout: () -> Unit): Unit {
    val SCOPE = rememberCoroutineScope()
    val USER_ID = SUPABASE.auth.currentSessionOrNull()?.user?.id ?: ""
    Column(modifier = Modifier.fillMaxSize()) {
        Button(onClick = onLogout) {
            Text("Cerrar Sesión (Logout)")
        }
    }
}

/**
 * Function that inserts into db a new movement with the data in the parameter
 * @param userId the id of the user that makes movement
 * @param quantity the import of the movement
 * @param category the category that bests describes the import
 * @param isExpense boolean to specify if its a expense or an earning
 * @param typeId the id of the type which the was made the movement
 * @return boolean if movement was successfull or not
 * @author Oriol Plazas León
 * @since 19/02/26
 */
suspend fun makeMovement(userId: String = "bd11d87c-7da1-4d80-9b51-90e72bc1d8f4", quantity: Double, category: String = "Varios", isExpense: Boolean, typeId: Int): Boolean{
    val NEW_MOVEMENT: Movement = Movement(null, userId, quantity, category, isExpense, typeId)
    try {
        SUPABASE.postgrest["movement"].insert(NEW_MOVEMENT)
        return true
    } catch (e: Exception){
        return false
    }
}

/**
 * Function that returns a Double number representing the total import of a List of Movement
 * @param list colection of Movement type where calculate the total import
 * @return the total of all movements
 * @author Oriol Plazas León
 * @since 19/02/26
 */
fun getTotalMoney(list: List<Movement>): Double {
    var total: Double = 0.0
    list.forEach {
        if (it.isexpense) {
            total -= it.import
        } else {
            total += it.import
        }
    }
    return total
}


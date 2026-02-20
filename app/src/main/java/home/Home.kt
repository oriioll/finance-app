package home
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.oriol.finance.ui.theme.AccentGreen
import dev.oriol.finance.ui.theme.Montserrat
import dev.oriol.finance.ui.theme.White

// IMPORTS CORREGIDOS PARA V3
import io.github.jan.supabase.auth.auth // <-- Cambiado de gotrue a auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.json.jsonPrimitive
import model.Movement
import supabase.SUPABASE



@Composable
fun Home(onLogout: () -> Unit): Unit {
    val SCOPE = rememberCoroutineScope()
    val USER = SUPABASE.auth.currentSessionOrNull()?.user
    val USER_ID = SUPABASE.auth.currentSessionOrNull()?.user?.id ?: ""
    val DISPLAY_NAME: String? = USER?.userMetadata?.get("display_name")?.jsonPrimitive?.content
    var moneyList by remember { mutableStateOf<List<Movement>>(emptyList()) }
    val TOTAL_AMOUNT: Double = getTotalMoney(moneyList)
    LaunchedEffect(Unit) {
        moneyList = getMovementList()
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = White,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = Montserrat
                    )
                ) {
                    append("Welcome, ")
                }

                withStyle(
                    style = SpanStyle(
                        color = AccentGreen,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Montserrat
                    )
                ) {
                    append(DISPLAY_NAME ?: "User")
                }
            }
        )
        Text(
            buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = AccentGreen,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Montserrat
                    )
                ) {
                    append("" + TOTAL_AMOUNT)
                }

                withStyle(
                    style = SpanStyle(
                        color = AccentGreen,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = Montserrat
                    )
                ) {
                    append("€")
                }
            }
        )

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
    val NEW_MOVEMENT: Movement = Movement(null, userId, category, isExpense, typeId, quantity)
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
            total -= it.amount
        } else {
            total += it.amount
        }
    }
    return total
}

/**
 * Function that gets from db a list<Movement> with all registers
 * @return movement list with all movements in db
 * @author Oriol Plazas Leon
 * @since 19/02/26
 */
suspend fun getMovementList(): List<Movement> {
    return SUPABASE.postgrest["movement"].select().decodeList<Movement>()
}


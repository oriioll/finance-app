package home

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.oriol.finance.ui.theme.AccentRed

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
    val USER_ID: String = SUPABASE.auth.currentSessionOrNull()?.user?.id ?: ""
    val DISPLAY_NAME: String? = USER?.userMetadata?.get("display_name")?.jsonPrimitive?.content
    var moneyList by remember { mutableStateOf<List<Movement>>(emptyList()) }
    val TOTAL_AMOUNT: Double = getTotalMoney(moneyList)
    LaunchedEffect(Unit) {
        moneyList = getMovementList(USER_ID)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WelcomeCard(DISPLAY_NAME, TOTAL_AMOUNT)
        RenderMovements(moneyList)
        Button(onClick = onLogout) {
            Text("Cerrar Sesión (Logout)")
        }
    }
}


/**
 * Function that renders a card with cards inside each movement of the list
 * @param moneyList the list of the money you want to render cards from
 * @author Oriol Plazas Leon
 * @since 12/03/2026
 */
@Composable
private fun RenderMovements(moneyList: List<Movement>): Unit {
    Card(
        modifier = Modifier
            .padding(vertical = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0x50707070)
        ),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (moneyList.isEmpty()) {
                Text("No hay movimientos aún", color = Color.Gray)
            }
            moneyList.forEach {
                var type: String = ""
                when (it.type_id) {
                    2 -> type = "Cash"
                    3 -> type = "Credit Card"
                    4 -> type = "Vinted"
                    5 -> type = "Binance"
                    6 -> type = "Wallapop"
                    7 -> type = "World App"
                }
                CreateMovementItem(it, type)

            }
        }
    }
}


/**
 * Function that creates a card of a movement - use it in a loop looping a list of Movement
 * @param m the Movement you want to create a card for
 * @param type the type of the movement (card, cash...)
 * @author Oriol Plazas León
 * @since 12/03/2026
 */
@Composable
private fun CreateMovementItem(m: Movement, type: String): Unit {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0x50707070)
        ),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(2.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "${m.amount}€",
                    color = if (m.isexpense) {
                        AccentRed
                    } else {
                        AccentGreen
                    },
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "${type}",
                    color = Color.Gray,
                    fontFamily = Montserrat,
                    fontSize = 11.sp
                )
            }

            Text(
                m.category,
                color = Color.White,
                fontFamily = Montserrat,
                fontSize = 12.sp
            )
        }
    }
}

/**
 * Function that renders a welcome ward with the username and the amount of money
 * @param DISPLAY_NAME the name of the user
 * @param TOTAL_AMOUNT the amount of money of the user
 * @author Oriol Plazas
 * @since 12/03/2025
 */
@Composable
private fun WelcomeCard(DISPLAY_NAME: String?, TOTAL_AMOUNT: Double): Unit {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color(0x50707070)
        ),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = Montserrat
                        )
                    ) {
                        append("Welcome, ")
                    }

                    withStyle(
                        style = SpanStyle(
                            color = AccentGreen,
                            fontSize = 22.sp,
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
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Montserrat
                        )
                    ) {
                        append("" + TOTAL_AMOUNT)
                    }

                    withStyle(
                        style = SpanStyle(
                            color = AccentGreen,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Montserrat
                        )
                    ) {
                        append("€")
                    }
                }
            )
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
suspend fun makeMovement(
    userId: String = "bd11d87c-7da1-4d80-9b51-90e72bc1d8f4",
    quantity: Double,
    category: String = "Varios",
    isExpense: Boolean,
    typeId: Int
): Boolean {
    val NEW_MOVEMENT: Movement = Movement(
        movement_id = null,
        user_id = userId,
        category = category,
        isexpense = isExpense,
        type_id = typeId,
        amount = quantity
    )
    try {
        SUPABASE.postgrest["movement"].insert(NEW_MOVEMENT)
        return true
    } catch (e: Exception) {
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
suspend fun getMovementList(userId: String): List<Movement> {
    return SUPABASE.postgrest["movement"].select {
        filter {
            eq("user_id", userId)
        }
    }.decodeList<Movement>()
}


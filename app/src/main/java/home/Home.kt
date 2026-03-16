package home

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import dev.oriol.finance.ui.theme.AccentRed
import dev.oriol.finance.ui.theme.BgDark

// IMPORTS CORREGIDOS PARA V3
import io.github.jan.supabase.auth.auth // <-- Cambiado de gotrue a auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
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
    var totalAmount: Double = getTotalMoney(moneyList)
    fun refreshList() {
        SCOPE.launch {
            moneyList = getMovementList(USER_ID).reversed()
        }
    }
    LaunchedEffect(Unit) {
        refreshList()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WelcomeCard(DISPLAY_NAME, totalAmount)
        RenderAddMovement(USER_ID, { refreshList() })
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
            .padding(vertical = 2.dp)
        ,
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.08f)
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
                    text = if(m.isexpense) "-${m.amount}€" else "+${m.amount}€",
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
                    text = type,
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
/**
 * Function that renders an add Movement card with a form to add a movement to the db
 * @param userId the id of the user you want to make the movement for
 * @param onMovementAdded the refresh function
 * @author Oriol Plazas
 * @since 16/03/2026
 */
private fun RenderAddMovement(userId: String, onMovementAdded: () -> Unit): Unit {
    val SCOPE = rememberCoroutineScope()
    val CONTEXT = LocalContext.current
    var amountText by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Varios") }
    var isExpense by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(false) }
    val typeOptions = mapOf(2 to "Cash", 3 to "Credit Card", 4 to "Vinted", 5 to "Binance", 6 to "Wallapop", 7 to "World App")
    var expandedType by remember { mutableStateOf(false) }
    var selectedTypeId by remember { mutableStateOf(2) }
    val expenseOptions = listOf("Expense", "Earning")
    var expandedExpense by remember { mutableStateOf(false) }
    var selectedExpenseOption by remember { mutableStateOf("Expense") }

    // Función auxiliar para colores uniformes
    @Composable
    fun textFieldColors() = OutlinedTextFieldDefaults.colors(
        focusedTextColor = White,
        unfocusedTextColor = White,
        focusedBorderColor = AccentGreen,
        unfocusedBorderColor = AccentGreen.copy(alpha = 0.4f),
        focusedLabelColor = AccentGreen,
        unfocusedLabelColor = White.copy(alpha = 0.6f)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0x50707070)),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), // Aumenté un pelín el padding exterior para que respire
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp) // Añadido un poco de espacio entre campos
        ) {
            Text("New movement", color = AccentGreen, fontFamily = Montserrat, fontSize = 16.sp, fontWeight = FontWeight.Bold)

            // FILA 1: Amount y Type
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Amount", fontSize = 12.sp) },
                    // ELIMINADO: .height(55.dp) - Ahora se ajusta solo
                    modifier = Modifier.weight(1f),
                    // REDUCIDO: De 20.sp a 16.sp para que no colapse el padding
                    textStyle = androidx.compose.ui.text.TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = if (isExpense) AccentRed else AccentGreen,
                        unfocusedTextColor = if (isExpense) AccentRed else AccentGreen,
                        focusedBorderColor = AccentGreen,
                        unfocusedBorderColor = AccentGreen.copy(alpha = 0.4f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                ExposedDropdownMenuBox(
                    expanded = expandedExpense,
                    onExpandedChange = { expandedExpense = !expandedExpense },
                    modifier = Modifier.weight(0.8f)
                ) {
                    OutlinedTextField(
                        value = selectedExpenseOption,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Type", fontSize = 12.sp) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedExpense) },
                        // ELIMINADO: .height(55.dp)
                        modifier = Modifier.menuAnchor(),
                        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp), // Texto un poco más pequeño
                        colors = textFieldColors(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(expanded = expandedExpense, onDismissRequest = { expandedExpense = false }) {
                        expenseOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option, fontSize = 14.sp) },
                                onClick = { selectedExpenseOption = option; isExpense = (option == "Expense"); expandedExpense = false }
                            )
                        }
                    }
                }
            }

            // FILA 2: Category
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category", fontSize = 12.sp) },
                // ELIMINADO: .height(55.dp)
                modifier = Modifier.fillMaxWidth(),
                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp),
                colors = textFieldColors(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            // FILA 3: Payment Method
            ExposedDropdownMenuBox(
                expanded = expandedType,
                onExpandedChange = { expandedType = !expandedType },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = typeOptions[selectedTypeId] ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Payment Method", fontSize = 12.sp) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedType) },
                    // ELIMINADO: .height(55.dp)
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp),
                    colors = textFieldColors(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(expanded = expandedType, onDismissRequest = { expandedType = false }) {
                    typeOptions.forEach { (id, label) ->
                        DropdownMenuItem(
                            text = { Text(label, fontSize = 14.sp) },
                            onClick = { selectedTypeId = id; expandedType = false }
                        )
                    }
                }
            }

            // BOTÓN DE GUARDAR
            ElevatedButton(
                onClick = {
                    try {
                        val amount = amountText.toDouble()
                        isLoading = true
                        SCOPE.launch {
                            val success = makeMovement(userId, amount, category, isExpense, selectedTypeId)
                            if (success) {
                                Toast.makeText(CONTEXT, "Added!", Toast.LENGTH_SHORT).show()
                                amountText = ""; onMovementAdded()
                            } else {
                                Toast.makeText(CONTEXT, "DB Error", Toast.LENGTH_SHORT).show()
                            }
                            isLoading = false
                        }
                    } catch (e: Exception) { Toast.makeText(CONTEXT, "Invalid format", Toast.LENGTH_SHORT).show() }
                },
                modifier = Modifier.fillMaxWidth(0.8f).padding(top = 8.dp), // Cambiado height por padding
                enabled = !isLoading,
                colors = ButtonDefaults.elevatedButtonColors(containerColor = AccentGreen, contentColor = BgDark),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) CircularProgressIndicator(color = BgDark, modifier = Modifier.size(20.dp))
                else Text("Add Movement", fontFamily = Montserrat, fontWeight = FontWeight.Bold)
            }
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

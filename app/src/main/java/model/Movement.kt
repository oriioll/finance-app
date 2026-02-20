package model
import kotlinx.serialization.Serializable

@Serializable
/**
 * Data class movement to generate movement objects that store the necessary data to update to db or to get from the db
 * @property movement_id serial id of each movement. Null by default since de db writes it
 * @property user_id the id of the user that makes the movement (user oriol by default)
 * @property amount the quantity of the import
 * @property category the category of the movement, can be such as "clothes", "food", "gift" etc...
 * @property isexpense boolean attribute that differences if the movement is an expense or an earning.
 * @property type_id id of the type of "payment" or "budget" related to the payment (can be ids for Cash, Vinted, Binance...)
 * @author Oriol Plazas León
 * @since 19/02/26
 */
data class Movement(
    var movement_id: Int? = null,
    var user_id: String,
    var category: String,
    var isexpense: Boolean,
    var type_id: Int,
    var amount: Double
)

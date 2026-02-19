package model
import kotlinx.serialization.Serializable

@Serializable
data class Movement(
    var user_id: String,
    var import: Double,
    var category: String,
    var isexpense: Boolean,
    var type_id: Int
)

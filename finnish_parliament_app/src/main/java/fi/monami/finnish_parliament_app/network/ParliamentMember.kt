package fi.monami.finnish_parliament_app.network
import kotlinx.serialization.Serializable

@Serializable
data class ParliamentMember(
    val personNumber: Int,
    val seatNumber: Int,
    val last: String,
    val first: String,
    val party: String,
    val minister: Boolean,
    val twitter: String,
    val bornYear: Int,
    val constituency: String
)
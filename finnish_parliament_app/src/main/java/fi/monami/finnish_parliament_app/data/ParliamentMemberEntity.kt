package fi.monami.finnish_parliament_app.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity("parliament_members")
data class ParliamentMemberEntity(
    @PrimaryKey
    val personNumber: Int,
    val seatNumber: Int,
    val last: String,
    val first: String,
    val party: String,
    val minister: Boolean,
    // Twitter may be an empty string, so use an empty default value.
    val twitter: String = "",
    val bornYear: Int,
    val constituency: String,
    // Picture is loaded from a separate API, so set a default initial value.
    val picture: String = ""
)
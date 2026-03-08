package fi.monami.finnish_parliament_app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("assessments")
data class AssessmentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val personNumber: Int,
    // Represents the direction of the assessment.
    // +1 indicates a positive evaluation.
    // -1 indicates a negative evaluation.
    // This value is chosen by the user when submitting a comment
    val indicator: Int,
    val text: String,
    val createdAt: Long = System.currentTimeMillis()
)
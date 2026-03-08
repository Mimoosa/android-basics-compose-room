package fi.monami.finnish_parliament_app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface AssessmentDao{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(assessmentEntity: AssessmentEntity)

    @Query("SELECT * from assessments WHERE personNumber = :personNumber")
    fun getAllAssessmentsForMember(personNumber: Int): Flow<List<AssessmentEntity>>

    // Returns the politician whose assessments have the highest total score,
    // calculated by summing all indicator values (+1 / -1) for each personNumber.
    // The result is ordered by score in descending order and limited to the top entry.
    @Query("""
    SELECT personNumber, SUM(indicator) AS score
    FROM assessments
    GROUP BY personNumber
    ORDER BY score DESC
    LIMIT 1
    """)
    fun getFavoritePolitician(): Flow<FavoritePoliticianScore>

}
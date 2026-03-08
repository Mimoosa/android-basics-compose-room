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
}
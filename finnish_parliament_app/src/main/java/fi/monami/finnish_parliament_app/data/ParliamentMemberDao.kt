package fi.monami.finnish_parliament_app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ParliamentMemberDao {
    // Insert member info (picture placeholder only)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(parliamentMemberEntity: ParliamentMemberEntity)

    @Query("SELECT * from parliament_members WHERE personNumber = :id")
    fun getParliamentMember(id: Int): Flow<ParliamentMemberEntity>

    @Query("SELECT * from parliament_members")
    fun getAllParliamentMembers(): Flow<List<ParliamentMemberEntity>>

    @Query("SELECT * from parliament_members WHERE party = :party")
    fun getAllPartyMembers(party: String): Flow<List<ParliamentMemberEntity>>
}
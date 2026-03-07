package fi.monami.finnish_parliament_app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ParliamentMemberDao {
    // Insert member info (picture placeholder only)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBasicInfo(parliamentMemberEntity: ParliamentMemberEntity)


}
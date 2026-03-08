package fi.monami.finnish_parliament_app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

@Database(entities = [ParliamentMemberEntity::class], version = 3, exportSchema = false)
abstract class ParliamentMemberDatabase: RoomDatabase() {
    abstract fun parliamentMemberDao(): ParliamentMemberDao

    companion object{
        @Volatile
        private var Instance: ParliamentMemberDatabase? = null

        fun getDatabase(context: Context): ParliamentMemberDatabase{
            return Instance?: synchronized(this){
                databaseBuilder(
                    context,
                    ParliamentMemberDatabase::class.java,
                    "parliament_member_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also{Instance = it}
            }
        }
    }
}
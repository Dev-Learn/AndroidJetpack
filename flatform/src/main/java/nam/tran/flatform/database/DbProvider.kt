package nam.tran.flatform.database

import androidx.room.Database
import androidx.room.RoomDatabase
import nam.tran.flatform.model.response.Comic

@Database(entities = [Comic::class], version = 1, exportSchema = false)
abstract class DbProvider : RoomDatabase() {
    abstract fun comicDao(): ComicDao
}

package nam.tran.flatform.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import nam.tran.flatform.model.response.Comic

@Database(entities = [Comic::class], version = 1, exportSchema = false)
@TypeConverters(ConvertData::class)
abstract class DbProvider : RoomDatabase() {
    abstract fun comicDao(): ComicDao
}

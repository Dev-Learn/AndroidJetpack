package nam.tran.flatform.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import nam.tran.flatform.model.response.Comic

/**
 * Interface for database access for Comic related operations.
 */
@Dao
interface ComicDao{

    @Query("SELECT * FROM comic")
    fun fetchSoccerSeason(): LiveData<List<Comic>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(season: Comic)
}
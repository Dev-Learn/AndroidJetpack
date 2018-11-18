package nam.tran.flatform.database

import androidx.lifecycle.LiveData
import androidx.room.*
import nam.tran.flatform.model.response.Comic

/**
 * Interface for database access for Comic related operations.
 */
@Dao
interface ComicDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(season: Comic)

    @Query("SELECT * FROM comic WHERE comic.id > (:id) LIMIT (:limit)")
    fun loadComic(id : Int = 0,limit : Int): List<Comic>

    @Delete
    fun delete(comic: Comic)
}
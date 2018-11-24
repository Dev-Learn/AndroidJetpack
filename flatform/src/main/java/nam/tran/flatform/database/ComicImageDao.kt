package nam.tran.flatform.database

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import nam.tran.flatform.model.response.BaseItemKey
import nam.tran.flatform.model.response.LinkComic

/**
 * Interface for database access for Comic related operations.
 */
@Dao
interface ComicImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<LinkComic>?)

    @Query("SELECT * FROM comicimage WHERE idcomic = (:idComic)")
    fun loadComicImage(idComic: Int): DataSource.Factory<Int,LinkComic>

    @Query("DELETE FROM comicimage WHERE idcomic = (:idComic)")
    fun delete(idComic: Int)
}
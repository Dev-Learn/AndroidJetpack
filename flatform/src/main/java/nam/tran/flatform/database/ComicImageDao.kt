package nam.tran.flatform.database

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.room.*
import nam.tran.flatform.model.response.BaseItemKey
import nam.tran.flatform.model.response.Comic
import nam.tran.flatform.model.response.LinkComic

/**
 * Interface for database access for Comic related operations.
 */
@Dao
interface ComicImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: ArrayList<LinkComic>?)

    @Query("SELECT * FROM comicimage WHERE id > (:id) AND idcomic = (:idComic) LIMIT (:limit)")
    fun loadComicImage(idComic : Int,id : Int = 0,limit : Int): LiveData<List<LinkComic>>

    @Query("DELETE FROM comicimage WHERE idcomic = (:idComic)")
    fun delete(idComic: Int)
}
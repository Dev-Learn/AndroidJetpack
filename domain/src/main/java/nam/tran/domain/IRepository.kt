package nam.tran.domain

import androidx.lifecycle.LiveData
import nam.tran.domain.entity.BaseItemKey
import nam.tran.domain.entity.ComicEntity
import nam.tran.domain.entity.LinkComicEntity
import nam.tran.domain.entity.state.Listing
import nam.tran.domain.entity.state.Resource

interface IRepository {

    fun getComic(offset: Int, count: Int, typeLoading: Int): LiveData<Resource<List<ComicEntity>>>

    fun getComicPage(convert: (List<ComicEntity>) -> List<Any>): LiveData<Listing<Any>>

    fun getComicItem(convert: (List<ComicEntity>) -> List<BaseItemKey>): LiveData<Listing<BaseItemKey>>

    fun getLinkComicItem(
        idComic: Int,
        convert: (List<LinkComicEntity>) -> List<BaseItemKey>
    ): Listing<BaseItemKey>
}

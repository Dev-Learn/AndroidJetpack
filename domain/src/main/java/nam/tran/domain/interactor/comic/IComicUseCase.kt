package nam.tran.domain.interactor.comic

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import nam.tran.domain.entity.ComicEntity
import nam.tran.domain.entity.LinkComicEntity
import nam.tran.domain.entity.state.Listing
import nam.tran.domain.entity.state.Resource
import nam.tran.flatform.model.response.BaseItemKey

interface IComicUseCase {
    fun getComic(offset: Int = 0, count: Int = 10, typeLoading: Int): LiveData<Resource<List<ComicEntity>>>
    fun getComicPage(convert: (List<ComicEntity>) -> List<BaseItemKey>): LiveData<Listing<PagedList<BaseItemKey>>>
    fun getComicItem(convert: (List<ComicEntity>) -> List<BaseItemKey>): LiveData<Listing<PagedList<BaseItemKey>>>
    fun getLinkComicItem(
        isDb: Boolean = false,
        idComic: Int,
        convert: (List<LinkComicEntity>) -> List<BaseItemKey>
    ): Listing<PagedList<BaseItemKey>>

    fun likeComic(entity: ComicEntity)
    fun loadComicLike(): LiveData<Resource<List<ComicEntity>>>
}
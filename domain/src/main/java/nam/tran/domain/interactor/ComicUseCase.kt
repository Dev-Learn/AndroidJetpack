package nam.tran.domain.interactor

import androidx.lifecycle.LiveData
import nam.tran.domain.IRepository
import nam.tran.flatform.model.response.BaseItemKey
import nam.tran.domain.entity.ComicEntity
import nam.tran.domain.entity.LinkComicEntity
import nam.tran.domain.entity.state.Listing
import nam.tran.domain.entity.state.Resource
import javax.inject.Inject

class ComicUseCase @Inject internal constructor(val iRepository: IRepository) {

    fun getComic(offset: Int = 0, count: Int = 10, typeLoading: Int): LiveData<Resource<List<ComicEntity>>> = iRepository.getComic(offset, count, typeLoading = typeLoading)

    fun getComicPage(convert: (List<ComicEntity>) -> List<Any>): LiveData<Listing<Any>> = iRepository.getComicPage(convert)

    fun getComicItem(convert: (List<ComicEntity>) -> List<BaseItemKey>): LiveData<Listing<BaseItemKey>> = iRepository.getComicItem(convert)

    fun getLinkComicItem(
        isDb: Boolean = false,
        idComic: Int,
        convert: (List<LinkComicEntity>) -> List<BaseItemKey>
    ): Listing<BaseItemKey> = iRepository.getLinkComicItem(isDb,idComic, convert)

    fun likeComic(entity: ComicEntity) = iRepository.likeComic(entity)

    fun loadComicLike(): LiveData<Resource<List<ComicEntity>>> = iRepository.loadComicLike()
}
package nam.tran.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.paging.toLiveData
import nam.tran.domain.entity.ComicEntity
import nam.tran.domain.entity.LinkComicEntity
import nam.tran.domain.entity.state.Listing
import nam.tran.domain.entity.state.Loading
import nam.tran.domain.entity.state.Resource
import nam.tran.domain.executor.AppExecutors
import nam.tran.domain.interactor.ItemComicDataSourceFactory
import nam.tran.domain.interactor.ItemLinkComicDataSourceFactory
import nam.tran.domain.interactor.ItemLinkComicDataSourceFactory2
import nam.tran.domain.interactor.PageDataSourceFactory
import nam.tran.domain.interactor.comic.IComicUseCase
import nam.tran.domain.interactor.core.DataBoundNetwork
import nam.tran.domain.mapper.DataEntityMapper
import nam.tran.flatform.IApi
import nam.tran.flatform.database.DbProvider
import nam.tran.flatform.model.response.BaseItemKey
import nam.tran.flatform.model.response.Comic
import retrofit2.Call
import tran.nam.util.Logger
import javax.inject.Inject

class ComicUseCase @Inject
internal constructor(
    private val appExecutors: AppExecutors,
    private val dataEntityMapper: DataEntityMapper,
    private val iApi: IApi,
    private val dbProvider: DbProvider
) : IComicUseCase {

    override fun getComic(offset: Int, count: Int, typeLoading: Int): LiveData<Resource<List<ComicEntity>>> {
        val request = object : DataBoundNetwork<List<Comic>, List<ComicEntity>>(appExecutors) {
            override fun convertData(body: List<Comic>?): List<ComicEntity>? {
                return dataEntityMapper.comicEntityMapper.transformEntity(body)
            }

            override fun statusLoading(): Int {
                return typeLoading
            }

            override fun createCall(): Call<List<Comic>> {
                return iApi.getComic(offset, count)
            }

        }
        request.fetchFromNetwork()
        return request.asLiveData()
    }

    override fun getComicPage(convert: (List<ComicEntity>) -> List<BaseItemKey>): LiveData<Listing<PagedList<BaseItemKey>>> {
        Logger.debug("Paging Learn Page", "Repository - getComic")
        val sourceFactory = PageDataSourceFactory(iApi, dataEntityMapper, convert)
        // We use toLiveData Kotlin extension function here, you could also use LivePagedListBuilder
        val livePagedList = sourceFactory.toLiveData(
            pageSize = 10,
            // provide custom executor for network requests, otherwise it will default to
            // Arch Components' IO pool which is also used for disk access
            fetchExecutor = appExecutors.networkIO()
        )
        val result = MutableLiveData<Listing<PagedList<BaseItemKey>>>()
        result.postValue(
            Listing(
                pagedList = livePagedList,
                networkState = Transformations.switchMap(sourceFactory.sourceLiveData) {
                    it.networkState
                }
            )
        )
        return result
    }

    override fun getComicItem(convert: (List<ComicEntity>) -> List<BaseItemKey>): LiveData<Listing<PagedList<BaseItemKey>>> {
        Logger.debug("Paging Learn Page", "Repository - getComic")

        val sourceFactory =
            ItemComicDataSourceFactory(iApi, dataEntityMapper, dbProvider, appExecutors.networkIO(), convert)
        // We use toLiveData Kotlin extension function here, you could also use LivePagedListBuilder
        val livePagedList = sourceFactory.toLiveData(
            // we use Config Kotlin ext. function here, could also use PagedList.Config.Builder
            config = Config(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSizeHint = 20
            ),
            fetchExecutor = appExecutors.networkIO()
        )
        val result = MutableLiveData<Listing<PagedList<BaseItemKey>>>()
        result.postValue(
            Listing(
                livePagedList,
                Transformations.switchMap(sourceFactory.sourceLiveData) {
                    it.networkState
                }
            )
        )
        return result
    }

    override fun getLinkComicItem(
        isDb: Boolean,
        idComic: Int,
        convert: (List<LinkComicEntity>) -> List<BaseItemKey>
    ): Listing<PagedList<BaseItemKey>> {
        val config = Config(
            pageSize = 20,
            enablePlaceholders = false,
            initialLoadSizeHint = 40
        )

        if (isDb) {

            val sourceFactory =
                ItemLinkComicDataSourceFactory2(idComic, iApi, appExecutors.networkIO(), 30, dbProvider)

            val livePagedList = dbProvider.comicImageDao().loadComicImage(
                idComic
            ).mapByPage {
                return@mapByPage convert(dataEntityMapper.linkComicEntityMapper.transform(it))
            }.toLiveData(
                pageSize = 10,
                boundaryCallback = sourceFactory
            )

            return Listing(
                livePagedList,
                sourceFactory.networkState
            )
        } else {
            val sourceFactory =
                ItemLinkComicDataSourceFactory(idComic, iApi, dataEntityMapper, convert)
            // We use toLiveData Kotlin extension function here, you could also use LivePagedListBuilder
            val livePagedList = sourceFactory.toLiveData(
                // we use Config Kotlin ext. function here, could also use PagedList.Config.Builder
                config = config,
                fetchExecutor = appExecutors.networkIO()
            )
            return Listing(
                pagedList = livePagedList,
                networkState = Transformations.switchMap(sourceFactory.sourceLiveData) {
                    it.networkState
                }
            )
        }
    }

    override fun likeComic(entity: ComicEntity) {
        appExecutors.networkIO().execute {
            if (entity.isLike) {
                dbProvider.comicDao().insert(dataEntityMapper.comicEntityMapper.transform(entity))
            } else {
                dbProvider.comicDao().delete(dataEntityMapper.comicEntityMapper.transform(entity))
                dbProvider.comicImageDao().delete(entity.id)
            }

        }
    }

    override fun loadComicLike(): LiveData<Resource<List<ComicEntity>>> {
        val result = MediatorLiveData<Resource<List<ComicEntity>>>()
        result.value = Resource.loading(null, Loading.LOADING_NORMAL)
        result.addSource(dbProvider.comicDao().loadComic()) {
            result.value =
                    Resource.success(dataEntityMapper.comicEntityMapper.transformEntity(it), Loading.LOADING_NORMAL)
        }
        return result
    }
}
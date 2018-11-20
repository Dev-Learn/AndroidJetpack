package nam.tran.domain

//import nam.tran.flatform.database.DbProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.Config
import androidx.paging.DataSource
import androidx.paging.ItemKeyedDataSource
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
import nam.tran.domain.interactor.core.DataBoundNetwork
import nam.tran.domain.mapper.DataEntityMapper
import nam.tran.flatform.IApi
import nam.tran.flatform.core.ApiResponse
import nam.tran.flatform.database.DbProvider
import nam.tran.flatform.local.IPreference
import nam.tran.flatform.model.response.BaseItemKey
import nam.tran.flatform.model.response.ComicResponse
import tran.nam.util.Logger
import javax.inject.Inject

class Repository @Inject
internal constructor(
    private val appExecutors: AppExecutors,
    private val iPreference: IPreference,
    private val dataEntityMapper: DataEntityMapper,
    private val iApi: IApi
    , private val dbProvider: DbProvider
) : IRepository {

    override fun getComic(offset: Int, count: Int, typeLoading: Int): LiveData<Resource<List<ComicEntity>>> {
        return object : DataBoundNetwork<List<ComicEntity>, ComicResponse>(appExecutors) {
            override fun convertData(body: ComicResponse?): List<ComicEntity>? {
                return dataEntityMapper.comicEntityMapper.transformEntity(body?.result)
            }

            override fun statusLoading(): Int {
                return typeLoading
            }

            override fun createCall(): LiveData<ApiResponse<ComicResponse>> {
                return iApi.getComic(offset, count)
            }

        }.asLiveData()
    }

    override fun getComicPage(convert: (List<ComicEntity>) -> List<Any>): LiveData<Listing<Any>> {
        Logger.debug("Paging Learn Page", "Repository - getComic")
        val sourceFactory = PageDataSourceFactory(iApi, dataEntityMapper, convert)
        // We use toLiveData Kotlin extension function here, you could also use LivePagedListBuilder
        val livePagedList = sourceFactory.toLiveData(
            pageSize = 10,
            // provide custom executor for network requests, otherwise it will default to
            // Arch Components' IO pool which is also used for disk access
            fetchExecutor = appExecutors.networkIO()
        )
        val result = MutableLiveData<Listing<Any>>()
        result.postValue(
            Listing(
                pagedList = livePagedList,
                networkState = Transformations.switchMap(sourceFactory.sourceLiveData) {
                    it.networkState
                }/*,
            retry = {
                sourceFactory.sourceLiveData.value?.retryAllFailed()
            },
            refresh = {
                sourceFactory.sourceLiveData.value?.invalidate()
            },
            refreshState = Transformations.switchMap(sourceFactory.sourceLiveData) {
                it.initialLoad
            }*/
            )
        )
        return result
    }

    override fun getComicItem(convert: (List<ComicEntity>) -> List<BaseItemKey>): LiveData<Listing<BaseItemKey>> {
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
        val result = MutableLiveData<Listing<BaseItemKey>>()
        result.postValue(
            Listing(
                pagedList = livePagedList,
                networkState = Transformations.switchMap(sourceFactory.sourceLiveData) {
                    it.networkState
                }/*,
            retry = {
                sourceFactory.sourceLiveData.value?.retryAllFailed()
            },
            refresh = {
                sourceFactory.sourceLiveData.value?.invalidate()
            },
            refreshState = Transformations.switchMap(sourceFactory.sourceLiveData) {
                it.initialLoad
            }*/
            )
        )
        return result
    }

    override fun getLinkComicItem(
        isDb: Boolean,
        idComic: Int,
        convert: (List<LinkComicEntity>) -> List<BaseItemKey>
    ): Listing<BaseItemKey> {

        val config = Config(
            pageSize = 20,
            enablePlaceholders = false,
            initialLoadSizeHint = 40,
            maxSize = 500
        )

        if (isDb) {

            val dataSource = object : DataSource.Factory<Int,BaseItemKey>(){
                override fun create(): DataSource<Int, BaseItemKey> {
                    return object : ItemKeyedDataSource<Int,BaseItemKey>(){
                        override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<BaseItemKey>) {
                            Transformations.map(dbProvider.comicImageDao().loadComicImage(idComic,0,params.requestedLoadSize)){
                                callback.onResult(convert(dataEntityMapper.linkComicEntityMapper.transform(it)))
                            }

                        }

                        override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<BaseItemKey>) {
                            Transformations.map(dbProvider.comicImageDao().loadComicImage(idComic,params.key,params.requestedLoadSize)){
                                callback.onResult(convert(dataEntityMapper.linkComicEntityMapper.transform(it)))
                            }
                        }

                        override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<BaseItemKey>) {

                        }

                        override fun getKey(item: BaseItemKey): Int {
                            return item.idKey
                        }

                    }
                }
            }

            val sourceFactory =
                ItemLinkComicDataSourceFactory2(idComic, iApi, appExecutors.networkIO(), 30, dbProvider)

            val livePagedList = dataSource.toLiveData(config = config,boundaryCallback = sourceFactory,fetchExecutor = appExecutors.networkIO())

            return Listing(
                pagedList = livePagedList,
                networkState = sourceFactory.networkState
                /*,
            retry = {
                sourceFactory.sourceLiveData.value?.retryAllFailed()
            },
            refresh = {
                sourceFactory.sourceLiveData.value?.invalidate()
            },
            refreshState = Transformations.switchMap(sourceFactory.sourceLiveData) {
                it.initialLoad
            }*/
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
                }/*,
            retry = {
                sourceFactory.sourceLiveData.value?.retryAllFailed()
            },
            refresh = {
                sourceFactory.sourceLiveData.value?.invalidate()
            },
            refreshState = Transformations.switchMap(sourceFactory.sourceLiveData) {
                it.initialLoad
            }*/
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

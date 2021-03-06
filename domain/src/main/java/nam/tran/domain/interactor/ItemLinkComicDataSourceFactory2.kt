package nam.tran.domain.interactor

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import nam.tran.domain.entity.state.ErrorResource
import nam.tran.domain.entity.state.Loading
import nam.tran.domain.entity.state.Resource
import nam.tran.flatform.IApi
import nam.tran.flatform.database.DbProvider
import nam.tran.flatform.model.response.BaseItemKey
import nam.tran.flatform.model.response.LinkComic
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tran.nam.util.Logger
import java.util.concurrent.Executor

class ItemLinkComicDataSourceFactory2(
    private val idComic: Int,
    private val iApi: IApi,
    private val ioExecutor: Executor,
    private val pageSize: Int,
    private val dbProvider: DbProvider
) : PagedList.BoundaryCallback<BaseItemKey>() {

    /**
     * There is no sync on the state because paging will always call loadInitial first then wait
     * for it to return some success value before calling loadAfter.
     */
    val networkState = MutableLiveData<Resource<BaseItemKey>>()

    override fun onZeroItemsLoaded() {
        Logger.debug("Paging Learn ItemKeyed", "loadInitial")
        networkState.postValue(Resource.loading(null, Loading.LOADING_NORMAL))
        iApi.getLinkComicPaging(limit = pageSize, id = idComic)
            .enqueue(object : Callback<List<LinkComic>> {
                override fun onFailure(call: Call<List<LinkComic>>, t: Throwable) {
                    networkState.postValue(
                        Resource.error(
                            ErrorResource(massage = t.message ?: "unknown err"),
                            null,
                            Loading.LOADING_NORMAL,
                            retry = {
                                onZeroItemsLoaded()
                            })
                    )
                }

                override fun onResponse(
                    call: Call<List<LinkComic>>,
                    response: Response<List<LinkComic>>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        networkState.postValue(Resource.success(null, Loading.LOADING_NORMAL))
                        ioExecutor.execute {
                            dbProvider.comicImageDao().insert(result)
                        }
                    } else {
                        networkState.postValue(
                            Resource.error(
                                ErrorResource(
                                    JSONObject(response.errorBody()?.string()).getString(
                                        "message"
                                    ), response.code()
                                ), null, Loading.LOADING_NORMAL, retry = {
                                    onZeroItemsLoaded()
                                })
                        )
                    }
                }

            })
    }

    override fun onItemAtEndLoaded(itemAtEnd: BaseItemKey) {
        Logger.debug("Paging Learn ItemKeyed", "loadAfter")
        networkState.postValue(Resource.loadingPaging(null, Loading.LOADING_NORMAL))
        iApi.getLinkComicPaging(idComic, itemAtEnd.idKey, pageSize)
            .enqueue(object : Callback<List<LinkComic>> {
                override fun onFailure(call: Call<List<LinkComic>>, t: Throwable) {
                    networkState.postValue(
                        Resource.errorPaging(
                            ErrorResource(massage = t.message ?: "unknown err"),
                            null,
                            Loading.LOADING_NORMAL,
                            retry = {
                                onItemAtEndLoaded(itemAtEnd)
                            })
                    )
                }

                override fun onResponse(
                    call: Call<List<LinkComic>>,
                    response: Response<List<LinkComic>>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        networkState.postValue(Resource.successPaging(null, Loading.LOADING_NORMAL))
                        ioExecutor.execute {
                            dbProvider.comicImageDao().insert(result)
                        }
                    } else {
                        networkState.postValue(
                            Resource.errorPaging(
                                ErrorResource(
                                    JSONObject(response.errorBody()?.string()).getString(
                                        "message"
                                    ), response.code()
                                ),
                                null,
                                Loading.LOADING_NORMAL,
                                retry = {
                                    onItemAtEndLoaded(itemAtEnd)
                                })
                        )
                    }
                }

            })
    }

    override fun onItemAtFrontLoaded(itemAtFront: BaseItemKey) {
        // ignored, since we only ever append to what's in the DB
    }
}
package nam.tran.domain.interactor

import androidx.lifecycle.MutableLiveData
import androidx.paging.ItemKeyedDataSource
import nam.tran.domain.entity.BaseItemKey
import nam.tran.domain.entity.LinkComicEntity
import nam.tran.domain.entity.state.NetworkState
import nam.tran.domain.mapper.DataEntityMapper
import nam.tran.flatform.IApi
import nam.tran.flatform.model.response.LinkComicResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tran.nam.util.Logger
import java.util.concurrent.Executor

class ItemKeyedComicLinkDataSource(
    private val idComic: Int,
    private val iApi: IApi,
    private val dataEntityMapper: DataEntityMapper,
    private val retryExecutor: Executor,
    private val convert: (List<LinkComicEntity>) -> List<BaseItemKey>
) : ItemKeyedDataSource<Int, BaseItemKey>() {

    // keep a function reference for the retry event
    private var retry: (() -> Any)? = null

    /**
     * There is no sync on the state because paging will always call loadInitial first then wait
     * for it to return some success value before calling loadAfter.
     */
    val networkState = MutableLiveData<NetworkState>()
    val initialLoad = MutableLiveData<NetworkState>()

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            retryExecutor.execute {
                it.invoke()
            }
        }
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<BaseItemKey>) {
        Logger.debug("Paging Learn ItemKeyed", "loadInitial")
        networkState.postValue(NetworkState.LOADING)
        val request = iApi.getLinkComicPaging(limit = params.requestedLoadSize, idComic = idComic)
        try {
            val response = request.execute()
            val result = response.body()
            if (result?.success!!) {
                val data = result.result
                retry = null
                networkState.postValue(NetworkState.LOADED)
                callback.onResult(convert(dataEntityMapper.linkComicEntityMapper.transform(data)))
            } else {
                retry = {
                    loadInitial(params, callback)
                }
                networkState.postValue(
                    NetworkState.error("error code: ${result.message}")
                )
            }
        } catch (e: Exception) {
            retry = {
                loadInitial(params, callback)
            }
            networkState.postValue(
                NetworkState.error("error code: ${e.message}")
            )
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<BaseItemKey>) {
        Logger.debug("Paging Learn ItemKeyed", "loadAfter")
        networkState.postValue(NetworkState.LOADING)
        iApi.getLinkComicPaging(params.key, params.requestedLoadSize, idComic)
            .enqueue(object : Callback<LinkComicResponse> {
                override fun onFailure(call: Call<LinkComicResponse>, t: Throwable) {
                    retry = {
                        loadAfter(params, callback)
                    }
                    networkState.postValue(NetworkState.error(t.message ?: "unknown err"))
                }

                override fun onResponse(
                    call: Call<LinkComicResponse>,
                    response: Response<LinkComicResponse>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result?.success!!) {
                            val data = result.result
                            retry = null
                            networkState.postValue(NetworkState.LOADED)
                            callback.onResult(convert(dataEntityMapper.linkComicEntityMapper.transform(data)))
                        } else {
                            retry = {
                                loadAfter(params, callback)
                            }
                            networkState.postValue(
                                NetworkState.error("error code: ${result.message}")
                            )
                        }
                    } else {
                        retry = {
                            loadAfter(params, callback)
                        }
                        networkState.postValue(
                            NetworkState.error("error code: ${response.code()}")
                        )
                    }
                }

            })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<BaseItemKey>) {
        // ignored, since we only ever append to our initial load
        Logger.debug("Paging Learn ItemKeyed", "loadBefore")
    }

    override fun getKey(item: BaseItemKey): Int {
        return item.idKey
    }
}
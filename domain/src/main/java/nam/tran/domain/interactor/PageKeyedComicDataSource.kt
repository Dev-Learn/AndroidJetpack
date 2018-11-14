package nam.tran.domain.interactor

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import nam.tran.domain.entity.ComicEntity
import nam.tran.domain.entity.state.NetworkState
import nam.tran.domain.mapper.DataEntityMapper
import nam.tran.flatform.IApi
import nam.tran.flatform.model.response.ComicResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tran.nam.util.Logger
import java.lang.Exception
import java.util.concurrent.Executor

class PageKeyedComicDataSource(
    private val iApi: IApi,
    private val dataEntityMapper: DataEntityMapper,
    private val retryExecutor: Executor,
    private val convert: (List<ComicEntity>) -> List<Any>
) : PageKeyedDataSource<Int, Any>() {

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

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Any>) {
        Logger.debug("Paging Learn","loadInitial")
        networkState.postValue(NetworkState.LOADING)
        val request = iApi.getComicPaging(0, params.requestedLoadSize)
        try {
            val response = request.execute()
            val data = response.body()?.result
            retry = null
            networkState.postValue(NetworkState.LOADED)
            callback.onResult(convert(dataEntityMapper.comicEntityMapper.transform(data)), 0,params.requestedLoadSize)
        }catch (e : Exception){
            retry = {
                loadInitial(params, callback)
            }
            networkState.postValue(
                NetworkState.error("error code: ${e.message}")
            )
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Any>) {
        Logger.debug("Paging Learn","loadAfter")
        networkState.postValue(NetworkState.LOADING)
        iApi.getComicPaging(params.key, params.requestedLoadSize).enqueue(object : Callback<ComicResponse> {
            override fun onFailure(call: Call<ComicResponse>, t: Throwable) {
                retry = {
                    loadAfter(params, callback)
                }
                networkState.postValue(NetworkState.error(t.message ?: "unknown err"))
            }

            override fun onResponse(
                call: Call<ComicResponse>,
                response: Response<ComicResponse>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()?.result
                    retry = null
                    networkState.postValue(NetworkState.LOADED)
                    callback.onResult(convert(dataEntityMapper.comicEntityMapper.transform(data)), params.key + params.requestedLoadSize)
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

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Any>) {
        // ignored, since we only ever append to our initial load
        Logger.debug("Paging Learn","loadBefore")
    }

}
package nam.tran.domain.interactor

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import nam.tran.domain.entity.ComicEntity
import nam.tran.domain.entity.core.BaseItemKey
import nam.tran.domain.entity.state.ErrorResource
import nam.tran.domain.entity.state.Loading
import nam.tran.domain.entity.state.Resource
import nam.tran.domain.mapper.DataEntityMapper
import nam.tran.flatform.IApi
import nam.tran.flatform.model.response.Comic
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tran.nam.util.Logger

class PageKeyedComicDataSource(
    private val iApi: IApi,
    private val dataEntityMapper: DataEntityMapper,
    private val convert: (List<ComicEntity>) -> List<BaseItemKey>
) : PageKeyedDataSource<Int, BaseItemKey>() {

    // keep a function reference for the retry event
//    private var retry: (() -> Any)? = null

    /**
     * There is no sync on the state because paging will always call loadInitial first then wait
     * for it to return some success value before calling loadAfter.
     */
    val networkState = MutableLiveData<Resource<*>>()

//    fun retryAllFailed() {
//        val prevRetry = retry
//        retry = null
//        prevRetry?.let {
//            retryExecutor.execute {
//                it.invoke()
//            }
//        }
//    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, BaseItemKey>) {
        Logger.debug("Paging Learn PageKeyed", "loadInitial")
        networkState.postValue(Resource.loading(null, Loading.LOADING_NORMAL))

        iApi.getComicPaging(0, params.requestedLoadSize).enqueue(object : Callback<List<Comic>> {
            override fun onFailure(call: Call<List<Comic>>, t: Throwable) {
                networkState.postValue(
                    Resource.error(
                        ErrorResource(massage = t.message ?: "unknown err"),
                        null,
                        Loading.LOADING_NORMAL,
                        retry = {
                            loadInitial(params, callback)
                        })
                )
            }

            override fun onResponse(
                call: Call<List<Comic>>,
                response: Response<List<Comic>>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    networkState.postValue(Resource.success(null, Loading.LOADING_NORMAL))
                    callback.onResult(
                        convert(dataEntityMapper.comicEntityMapper.transformEntity(result)),
                        0,
                        params.requestedLoadSize
                    )
                } else {
                    networkState.postValue(
                        Resource.error(
                            ErrorResource(
                                JSONObject(response.errorBody()?.string()).getString("message"),
                                response.code()
                            ),
                            null,
                            Loading.LOADING_NORMAL,
                            retry = {
                                loadInitial(params, callback)
                            })
                    )
                }
            }

        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, BaseItemKey>) {
        Logger.debug("Paging Learn PageKeyed", "loadAfter")
        networkState.postValue(Resource.loading(null, Loading.LOADING_NORMAL))
        iApi.getComicPaging(params.key, params.requestedLoadSize).enqueue(object : Callback<List<Comic>> {
            override fun onFailure(call: Call<List<Comic>>, t: Throwable) {
                networkState.postValue(
                    Resource.error(
                        ErrorResource(massage = t.message ?: "unknown err"),
                        null,
                        Loading.LOADING_NORMAL,
                        retry = {
                            loadAfter(params, callback)
                        })
                )
            }

            override fun onResponse(
                call: Call<List<Comic>>,
                response: Response<List<Comic>>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    networkState.postValue(Resource.success(null, Loading.LOADING_NORMAL))
                    callback.onResult(
                        convert(dataEntityMapper.comicEntityMapper.transformEntity(result)),
                        params.key + params.requestedLoadSize
                    )
                } else {
                    networkState.postValue(
                        Resource.error(
                            ErrorResource(
                                JSONObject(response.errorBody()?.string()).getString("message"),
                                response.code()
                            ),
                            null,
                            Loading.LOADING_NORMAL,
                            retry = {
                                loadAfter(params, callback)
                            })
                    )
                }
            }

        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, BaseItemKey>) {
        // ignored, since we only ever append to our initial load
        Logger.debug("Paging Learn PageKeyed", "loadBefore")
    }

}
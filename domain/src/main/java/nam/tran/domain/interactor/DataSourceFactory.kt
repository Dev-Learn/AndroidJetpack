package nam.tran.domain.interactor

import android.arch.lifecycle.MediatorLiveData
import android.arch.paging.DataSource
import android.arch.paging.ItemKeyedDataSource
import android.support.annotation.MainThread
import nam.tran.domain.entity.state.Resource
import nam.tran.domain.executor.AppExecutors
import nam.tran.domain.interactor.core.IDataBoundResource
import nam.tran.flatform.core.ApiEmptyResponse
import nam.tran.flatform.core.ApiErrorResponse
import nam.tran.flatform.core.ApiSuccessResponse

abstract class DataSourceFactory<Request, ResultType, RequestType> @MainThread constructor(private val appExecutors: AppExecutors) :
    DataSource.Factory<Request, RequestType>(), IDataBoundResource<RequestType> {

    val result = MediatorLiveData<Resource<ResultType>>()

    override fun create(): DataSource<Request, RequestType> {
        return object : ItemKeyedDataSource<Request,RequestType>(){
            override fun loadInitial(params: LoadInitialParams<Request>, callback: LoadInitialCallback<RequestType>) {
                result.value = Resource.loading(null, statusLoading())
                fetchFromNetwork()
            }

            override fun loadAfter(params: LoadParams<Request>, callback: LoadCallback<RequestType>) {

            }

            override fun loadBefore(params: LoadParams<Request>, callback: LoadCallback<RequestType>) {

            }

            override fun getKey(item: RequestType): Request {
                return getRequest(item)
            }

        }
    }

    private fun fetchFromNetwork() {
        val apiResponse = createCall()
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            when (response) {
                is ApiSuccessResponse -> {
                    appExecutors.diskIO().execute {
                        appExecutors.mainThread().execute {
                            setValue(Resource.success(convertData(response.body), statusLoading()))
                        }
                    }
                }
                is ApiEmptyResponse -> {
                    appExecutors.mainThread().execute {
                        setValue(Resource.success(null, statusLoading()))
                    }
                }
                is ApiErrorResponse -> {
                    onFetchFailed()
                    setValue(Resource.error(response.errorMessage, null, statusLoading()))
                }
            }
        }
    }

    @MainThread
    fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    protected open fun onFetchFailed() {}

    abstract fun convertData(body: RequestType?): ResultType?

    abstract fun getRequest(item: RequestType): Request
}
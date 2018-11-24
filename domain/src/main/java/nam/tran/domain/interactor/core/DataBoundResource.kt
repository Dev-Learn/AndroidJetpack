///*
// * Copyright (C) 2017 The Android Open Source Project
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package nam.tran.domain.interactor.core
//
//import androidx.annotation.MainThread
//import androidx.annotation.WorkerThread
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MediatorLiveData
//import nam.tran.domain.entity.state.Loading
//import nam.tran.domain.entity.state.Resource
//import nam.tran.domain.executor.AppExecutors
//import org.json.JSONObject
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//
///**
// * A generic class that can provide a resource backed by both the sqlite database and the network.
// *
// *
// * You can read more about it in the [Architecture
// * Guide](https://developer.android.com/arch).
// * @param <ResultType>
// * @param <RequestType>
//</RequestType></ResultType> */
//@Suppress("LeakingThis")
//abstract class DataBoundResource<ResultType, RequestType>
//@MainThread constructor(private val appExecutors: AppExecutors) : IDataBoundResource<RequestType> {
//
//    private val result = MediatorLiveData<Resource<ResultType>>()
//
//    init {
//        result.value = Resource.loading(null, statusLoading())
//        val dbSource = loadFromDb()
//        result.addSource(dbSource) { data ->
//            result.removeSource(dbSource)
//            if (shouldFetch(data)) {
//                fetchFromNetwork(dbSource)
//            } else {
//                result.addSource(dbSource) { newData ->
//                    setValue(Resource.success(newData, statusLoading()))
//                }
//            }
//        }
//    }
//
//    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
//        val apiResponse = createCall()
//
//        result.addSource(dbSource) { newData ->
//            setValue(Resource.loading(newData, statusLoading()))
//        }
//
//        result.addSource(apiResponse) {
//            result.removeSource(apiResponse)
//            result.removeSource(dbSource)
//            it.enqueue(object : Callback<RequestType> {
//                override fun onFailure(call: Call<RequestType>, t: Throwable) {
//                    Resource.error(
//                        t.message ?: "unknown err",
//                        null,
//                        Loading.LOADING_NORMAL,
//                        retry = {
//                            fetchFromNetwork(dbSource)
//                        })
//                }
//
//                override fun onResponse(call: Call<RequestType>, response: Response<RequestType>) {
//                    if (response.isSuccessful) {
//                        appExecutors.diskIO().execute {
//                            saveCallResult(processResponse(response))
//                            appExecutors.mainThread().execute {
//                                // we specially request a new live data,
//                                // otherwise we will get immediately last cached value,
//                                // which may not be updated with latest results received from network.
//                                result.addSource(loadFromDb()) { newData ->
//                                    setValue(Resource.success(newData, statusLoading()))
//                                }
//                            }
//                        }
//                    } else {
//                        onFetchFailed()
//                        result.addSource(dbSource) { newData ->
//                            setValue(
//                                Resource.error(
//                                    JSONObject(response.errorBody()?.string()).getString("message"),
//                                    newData,
//                                    statusLoading(),
//                                    retry = {
//                                        fetchFromNetwork(dbSource)
//                                    })
//                            )
//                        }
//                    }
//                }
//
//            })
//        }
//    }
//
//    @MainThread
//    fun setValue(newValue: Resource<ResultType>) {
//        if (result.value != newValue) {
//            result.value = newValue
//        }
//    }
//
//    fun asLiveData() = result as LiveData<Resource<ResultType>>
//
//    protected open fun onFetchFailed() {}
//
//    @WorkerThread
//    protected open fun processResponse(response: Response<RequestType>) = response.body()
//
//    @WorkerThread
//    protected abstract fun saveCallResult(item: RequestType?)
//
//    @MainThread
//    protected abstract fun shouldFetch(data: ResultType?): Boolean
//
//    @MainThread
//    protected abstract fun loadFromDb(): LiveData<ResultType>
//}

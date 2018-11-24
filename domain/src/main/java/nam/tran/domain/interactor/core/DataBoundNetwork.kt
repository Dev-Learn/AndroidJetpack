/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nam.tran.domain.interactor.core

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import nam.tran.domain.entity.state.Loading
import nam.tran.domain.entity.state.Resource
import nam.tran.domain.executor.AppExecutors
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 *
 *
 * You can read more about it in the [Architecture
 * Guide](https://developer.android.com/arch).
 * @param <ResultType>
 * @param <RequestType>
</RequestType></ResultType> */
@Suppress("LeakingThis")
abstract class DataBoundNetwork<ResultType, RequestType>
@MainThread constructor(private val appExecutors: AppExecutors) : IDataBoundResource<RequestType> {

    val result = MutableLiveData<Resource<ResultType>>()

    init {
        result.value = Resource.loading(null, statusLoading())
        fetchFromNetwork()
    }

    private fun fetchFromNetwork() {
        createCall().enqueue(object : Callback<RequestType> {
            override fun onFailure(call: Call<RequestType>, t: Throwable) {
                setValue(Resource.error(
                    t.message ?: "unknown err",
                    null,
                    statusLoading(),
                    retry = {
                        fetchFromNetwork()
                    }))
            }

            override fun onResponse(call: Call<RequestType>, response: Response<RequestType>) {
                if (response.isSuccessful) {
                    setValue(Resource.success(convertData(response.body()), statusLoading()))
                } else {
                    onFetchFailed()
                    setValue(
                        Resource.error(
                            JSONObject(response.errorBody()?.string()).getString("message"),
                            null,
                            statusLoading(),
                            retry = {
                                fetchFromNetwork()
                            })
                    )
                }
            }

        })
    }

    @MainThread
    fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    protected open fun onFetchFailed() {}

    abstract fun convertData(body: RequestType?): ResultType?
}

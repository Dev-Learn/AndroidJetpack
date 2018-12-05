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
import androidx.lifecycle.MutableLiveData
import nam.tran.domain.entity.state.ErrorResource
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
 * @param <ResultEntity>
 * @param <Result>
</Result></ResultEntity> */
@Suppress("LeakingThis")
abstract class DataBoundNetwork<Result,ResultEntity>
@MainThread constructor(private val appExecutors: AppExecutors) : IDataBoundResource<Result> {

    val result = MutableLiveData<Resource<ResultEntity>>()

    fun fetchFromNetwork() {
        result.postValue(if (isPaging()) Resource.loadingPaging<ResultEntity>(null, statusLoading()) else Resource.loading<ResultEntity>(
            null,
            statusLoading()
        ))
        createCall().enqueue(object : Callback<Result> {
            override fun onFailure(call: Call<Result>, t: Throwable) {
                setValue(if (isPaging())
                    Resource.errorPaging<ResultEntity>(
                        ErrorResource(massage = t.message ?: "unknown err"),
                        null,
                        statusLoading(),
                        retry = {
                            fetchFromNetwork()
                        })
                else
                    Resource.error<ResultEntity>(
                        ErrorResource(massage = t.message ?: "unknown err"),
                        null,
                        statusLoading(),
                        retry = {
                            fetchFromNetwork()
                        }))
            }

            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                if (response.isSuccessful) {
                    setValue(if (isPaging()) Resource.successPaging(
                        convertData(response.body()),
                        statusLoading()
                    ) else Resource.success(convertData(response.body()), statusLoading()))
                } else {
                    onFetchFailed()
                    setValue(if (isPaging()) Resource.errorPaging<ResultEntity>(
                        ErrorResource(
                            JSONObject(response.errorBody()?.string()).getString("message"),
                            response.code()
                        ),
                        null,
                        statusLoading(),
                        retry = {
                            fetchFromNetwork()
                        }) else Resource.error<ResultEntity>(
                        ErrorResource(
                            JSONObject(response.errorBody()?.string()).getString("message"),
                            response.code()
                        ),
                        null,
                        statusLoading(),
                        retry = {
                            fetchFromNetwork()
                        }))
                }
            }

        })
    }

    @MainThread
    fun setValue(newValue: Resource<ResultEntity>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    fun asLiveData() = result as LiveData<Resource<ResultEntity>>

    protected open fun onFetchFailed() {}

    protected open fun isPaging(): Boolean {
        return false
    }

    abstract fun convertData(body: Result?): ResultEntity?
}

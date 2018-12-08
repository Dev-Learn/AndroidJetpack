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

package nam.tran.domain.entity.state

import nam.tran.domain.entity.state.Status.*
import tran.nam.util.Constant.Companion.EMPTY

/**
 * A generic class that holds a value with its loading status.
 *
 * @param <T>
</T> */
class Resource<T>(
    @Status val status: Int, val data: T?, val errorResource: ErrorResource?, @Loading var loading: Int,
    val retry: (() -> Unit)?
) {

    var initial = true

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }

        val resource = other as Resource<*>?

        return (status == resource!!.status
                && (if (errorResource != null) errorResource == resource.errorResource else resource.errorResource == null)
                && if (data != null) data == resource.data else resource.data == null)
    }

    override fun hashCode(): Int {
        var result = status
        result = 31 * result + (errorResource?.hashCode() ?: 0)
        result = 31 * result + (data?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Resource{" +
                "status=" + getStatus(status) + "\n" +
                "loading=" + getLoading(loading) + "\n" +
                ", message='" + errorResource + '\''.toString() + "\n" +
                ", data=" + data +
                '}'.toString()
    }

    private fun getStatus(@Status status: Int): String {
        when (status) {
            ERROR -> return "ErrorResource"
            LOADING -> return "Loading"
            SUCCESS -> return "Success"
        }
        return EMPTY
    }

    private fun getLoading(@Loading loading: Int): String {
        when (loading) {
            Loading.LOADING_DIALOG -> return "Loading Dialog"
            Loading.LOADING_NONE -> return "Loading None"
            Loading.LOADING_NORMAL -> return "Loading Normal"
        }
        return EMPTY
    }

    fun isSuccess(): Boolean {
        return status == Status.SUCCESS
    }

    companion object {

        @JvmStatic
        fun <T> success(data: T?, loading: Int = Loading.LOADING_NORMAL): Resource<T> {
            return Resource(SUCCESS, data, null, loading, null)
        }

        @JvmStatic
        fun <T> successPaging(data: T?, loading: Int = Loading.LOADING_NORMAL): Resource<T> {
            val resource = Resource(SUCCESS, data, null, loading, null)
            resource.initial = false
            return resource
        }

        @JvmStatic
        fun <T> error(msg: ErrorResource?, data: T?, loading: Int = Loading.LOADING_NORMAL, retry: () -> Unit): Resource<T> {
            return Resource(ERROR, data, msg, loading, retry)
        }

        @JvmStatic
        fun <T> errorPaging(msg: ErrorResource?, data: T?, loading: Int = Loading.LOADING_NORMAL, retry: () -> Unit): Resource<T> {
            val resource = Resource(ERROR, data, msg, loading, retry)
            resource.initial = false
            return resource
        }

        @JvmStatic
        fun <T> loading(data: T?, @Loading loading: Int = Loading.LOADING_NORMAL): Resource<T> {
            return Resource(LOADING, data, null, loading, null)
        }

        @JvmStatic
        fun <T> loadingPaging(data: T?, @Loading loading: Int = Loading.LOADING_NORMAL): Resource<T> {
            val resource = Resource(LOADING, data, null, loading, null)
            resource.initial = false
            return resource
        }
    }
}

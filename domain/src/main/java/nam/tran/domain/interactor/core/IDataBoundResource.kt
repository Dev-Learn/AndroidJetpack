package nam.tran.domain.interactor.core

import androidx.lifecycle.LiveData
import androidx.annotation.MainThread
import nam.tran.flatform.core.ApiResponse
import retrofit2.Call
import retrofit2.Response

interface IDataBoundResource<RequestType>{

    fun statusLoading(): Int

    fun createCall(): Call<RequestType>
}
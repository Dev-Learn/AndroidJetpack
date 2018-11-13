package nam.tran.domain.interactor.core

import androidx.lifecycle.LiveData
import androidx.annotation.MainThread
import nam.tran.flatform.core.ApiResponse

interface IDataBoundResource<RequestType>{

    fun statusLoading(): Int

    @MainThread
    fun createCall(): LiveData<ApiResponse<RequestType>>
}
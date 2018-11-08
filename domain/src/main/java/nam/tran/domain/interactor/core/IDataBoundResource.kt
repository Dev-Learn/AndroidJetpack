package nam.tran.domain.interactor.core

import android.arch.lifecycle.LiveData
import android.support.annotation.MainThread
import nam.tran.flatform.core.ApiResponse

interface IDataBoundResource<RequestType>{

    fun statusLoading(): Int

    @MainThread
    fun createCall(): LiveData<ApiResponse<RequestType>>
}
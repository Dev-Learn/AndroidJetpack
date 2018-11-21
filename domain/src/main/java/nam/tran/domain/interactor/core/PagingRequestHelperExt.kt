package nam.tran.domain.interactor.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nam.tran.domain.entity.state.Loading
import nam.tran.domain.entity.state.Resource
import nam.tran.flatform.model.response.BaseItemKey

private fun getErrorMessage(report: PagingRequestHelper.StatusReport): String {
    return PagingRequestHelper.RequestType.values().mapNotNull {
        report.getErrorFor(it)?.message
    }.first()
}

fun PagingRequestHelper.createStatusLiveData(): LiveData<Resource<BaseItemKey>> {
    val liveData = MutableLiveData<Resource<BaseItemKey>>()
    addListener { report ->
        when {
            report.hasRunning() -> liveData.postValue(Resource.loadingPaging(null, Loading.LOADING_NORMAL))
            report.hasError() -> liveData.postValue(
                Resource.errorPaging(getErrorMessage(report), null, Loading.LOADING_NORMAL, retry = {

                })
            )
            else -> liveData.postValue(Resource.successPaging(null, Loading.LOADING_NORMAL))
        }
    }
    return liveData
}

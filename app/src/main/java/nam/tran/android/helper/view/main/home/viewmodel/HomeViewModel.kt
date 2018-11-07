package nam.tran.android.helper.view.main.home.viewmodel;

import android.app.Application

import javax.inject.Inject

import tran.nam.core.viewmodel.BaseFragmentViewModel
import tran.nam.core.viewmodel.IProgressViewModel
import nam.tran.domain.entity.state.Resource
import android.arch.lifecycle.MutableLiveData

class HomeViewModel @Inject internal constructor(application: Application) : BaseFragmentViewModel(application),
    IProgressViewModel {

    private val results = MutableLiveData<Resource<String>>()

    override fun resource(): Resource<*>? {
        return results.value
    }


}

package nam.tran.android.helper.view.home.user.viewmodel;

import android.app.Application
import androidx.lifecycle.MutableLiveData
import nam.tran.android.helper.mapper.DataMapper
import nam.tran.domain.entity.state.Resource
import nam.tran.domain.interactor.app.IAppUseCase
import tran.nam.core.viewmodel.BaseFragmentViewModel
import tran.nam.core.viewmodel.IProgressViewModel
import javax.inject.Inject

class UserViewModel @Inject internal constructor(
    application: Application, val iAppUseCase: IAppUseCase,
    val dataMapper: DataMapper
) : BaseFragmentViewModel(application),
    IProgressViewModel {

    private val results = MutableLiveData<Resource<*>>()

    override fun resource(): Resource<*>? {
        return results.value
    }

    fun logout() {
        dataMapper.preferenceMapper.transform(iAppUseCase.logout())
    }

}

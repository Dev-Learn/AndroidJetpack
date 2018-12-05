package nam.tran.android.helper.view.forgotPassword.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import nam.tran.domain.entity.state.Resource
import nam.tran.domain.interactor.login.ILoginUseCase
import tran.nam.core.viewmodel.BaseFragmentViewModel
import tran.nam.core.viewmodel.IProgressViewModel
import javax.inject.Inject

class ForgotPasswordViewModel @Inject internal constructor(
    application: Application,
    private val iLoginUseCase: ILoginUseCase
) :
    BaseFragmentViewModel(application), IProgressViewModel {

    val results = MutableLiveData<Resource<String>>()

    override fun resource(): Resource<String>? {
        return results.value
    }

    fun requestPassword(email: String) {
        view<IForgotPasswordView>()?.let { v ->
            iLoginUseCase.forgotPassword(email).observe(v, Observer {
                results.postValue(it)
            })
        }
    }

}

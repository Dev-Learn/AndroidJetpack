package nam.tran.android.helper.view.login.viewmodel;

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import nam.tran.android.helper.mapper.DataMapper
import nam.tran.domain.entity.state.Resource
import nam.tran.domain.interactor.app.IAppUseCase
import nam.tran.domain.interactor.login.ILoginUseCase
import tran.nam.core.viewmodel.BaseFragmentViewModel
import javax.inject.Inject

class LoginViewModel @Inject internal constructor(
    application: Application, private val iLoginUseCase: ILoginUseCase
    , private val iAppUseCase: IAppUseCase,
    private val dataMapper: DataMapper
) :
    BaseFragmentViewModel(application) {

    val login = MutableLiveData<Resource<Void>?>()
    val resendEmail = MutableLiveData<Resource<Void>?>()

    fun login(email: String, password: String) {
        view<ILoginViewModel>()?.let { v ->
            iLoginUseCase.login(email, password).observe(v, Observer {
                login.postValue(it)
            })
        }
    }

    fun resendVerifyEmail(email: String, password: String) {
        view<ILoginViewModel>()?.let { v ->
            iLoginUseCase.send_email_verify(email, password).observe(v, Observer {
                resendEmail.postValue(it)
            })
        }
    }

    override fun onStop() {
        super.onStop()
        login.postValue(null)
        resendEmail.postValue(null)
    }

    fun loginSuccess() {
        dataMapper.preferenceMapper.transform(iAppUseCase.getPreference())
    }
}

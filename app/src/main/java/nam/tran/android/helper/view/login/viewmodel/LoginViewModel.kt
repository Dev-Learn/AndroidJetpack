package nam.tran.android.helper.view.login.viewmodel;

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import nam.tran.android.helper.mapper.DataMapper
import nam.tran.domain.entity.state.Resource
import nam.tran.domain.interactor.app.IAppUseCase
import nam.tran.domain.interactor.login.ILoginUseCase
import tran.nam.core.viewmodel.BaseFragmentViewModel
import tran.nam.core.viewmodel.IProgressViewModel
import javax.inject.Inject

class LoginViewModel @Inject internal constructor(
    application: Application, private val iLoginUseCase: ILoginUseCase
    , val iAppUseCase: IAppUseCase,
    val dataMapper: DataMapper
) :
    BaseFragmentViewModel(application),
    IProgressViewModel {

    val results = MutableLiveData<Resource<Void>?>()

    var type = TYPE.LOGIN

    override fun resource(): Resource<Void>? {
        return results.value
    }

    fun login(email: String, password: String) {
        view<ILoginViewModel>()?.let { v ->
            iLoginUseCase.login(email, password).observe(v, Observer {
                results.postValue(it)
            })
        }
    }

    fun resendVerifyEmail(email: String, password: String) {
        view<ILoginViewModel>()?.let { v ->
            type = TYPE.VERIFY_EMAIL
            iLoginUseCase.send_email_verify(email, password).observe(v, Observer {
                results.postValue(it)
            })
        }
    }

    fun loginSuccess() {
        dataMapper.preferenceMapper.transform(iAppUseCase.getPreference())
    }

    enum class TYPE {
        LOGIN, VERIFY_EMAIL
    }

}

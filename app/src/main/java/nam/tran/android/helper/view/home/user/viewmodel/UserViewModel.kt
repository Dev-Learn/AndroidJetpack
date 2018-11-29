package nam.tran.android.helper.view.home.user.viewmodel;

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import nam.tran.android.helper.mapper.DataMapper
import nam.tran.android.helper.model.UserModel
import nam.tran.android.helper.view.home.user.viewmodel.UserViewModel.TYPE.INFO
import nam.tran.android.helper.view.home.user.viewmodel.UserViewModel.TYPE.UPDATE_INFO
import nam.tran.domain.entity.state.Resource
import nam.tran.domain.interactor.app.IAppUseCase
import nam.tran.domain.interactor.user.IUserUseCase
import tran.nam.core.viewmodel.BaseFragmentViewModel
import tran.nam.core.viewmodel.IProgressViewModel
import java.io.File
import javax.inject.Inject

class UserViewModel @Inject internal constructor(
    application: Application, val iAppUseCase: IAppUseCase,
    val dataMapper: DataMapper, val iUserUseCase: IUserUseCase
) : BaseFragmentViewModel(application),
    IProgressViewModel {

    val mApp = application

    lateinit var user: UserModel

    var type = INFO

    val results = MutableLiveData<Resource<UserModel>>()

    override fun resource(): Resource<*>? {
        return results.value
    }

    override fun onCreated() {
        view<IUserViewModel>()?.let { v ->
            Transformations.map(iUserUseCase.getUserInfo()) {
                dataMapper.userModelMapper.transform(it)
            }.observe(v, Observer {
                results.postValue(it)
            })
        }
    }

    fun logout() {
        dataMapper.preferenceMapper.transform(iAppUseCase.logout())
    }

    fun updateInfo(name: String, userModel: UserModel) {
        view<IUserViewModel>()?.let { v ->
            type = UPDATE_INFO
            val uri = user.uri
            val file = File(uri?.path)
            val type = mApp.contentResolver.getType(uri)
            val update = Transformations.map(iUserUseCase.updateUserInfo(userModel.id, name, file, type)) {
                dataMapper.userModelMapper.transform(it)
            }
            update.observe(v, Observer {
                results.value = it
            })
        }
    }

    enum class TYPE {
        INFO, UPDATE_INFO
    }

}

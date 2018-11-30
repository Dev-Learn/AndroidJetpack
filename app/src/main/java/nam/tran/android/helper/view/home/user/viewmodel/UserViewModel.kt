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
import tran.nam.util.FilePickUtils
import java.io.File
import java.net.URI
import javax.inject.Inject

class UserViewModel @Inject internal constructor(
    application: Application, val iAppUseCase: IAppUseCase,
    val dataMapper: DataMapper, val iUserUseCase: IUserUseCase
) : BaseFragmentViewModel(application),
    IProgressViewModel {

    val mApp = application

    var user: UserModel? = null

    var type = INFO

    val results = MutableLiveData<Resource<UserModel>>()

    override fun resource(): Resource<*>? {
        return results.value
    }

    override fun onCreated() {
        view<IUserViewModel>()?.let { v ->
            if (user == null){
                Transformations.map(iUserUseCase.getUserInfo()) {
                    dataMapper.userModelMapper.transform(it)
                }.observe(v, Observer {
                    results.postValue(it)
                })
            }
        }
    }

    fun logout() {
        dataMapper.preferenceMapper.transform(iAppUseCase.logout())
    }

    fun updateInfo(name: String, userModel: UserModel) {
        view<IUserViewModel>()?.let { v ->
            type = UPDATE_INFO
            val uri = user?.uri
            uri?.let {
                val path = FilePickUtils.getPath(mApp, uri) ?: ""
                Transformations.map(iUserUseCase.updateUserInfo(userModel.id, name, File(path))) {
                    dataMapper.userModelMapper.transform(it)
                }.observe(v, Observer {
                    results.value = it
                })
            }

        }
    }

    enum class TYPE {
        INFO, UPDATE_INFO
    }

}

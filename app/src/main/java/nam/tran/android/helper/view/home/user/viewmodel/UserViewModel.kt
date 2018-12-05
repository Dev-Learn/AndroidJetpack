package nam.tran.android.helper.view.home.user.viewmodel;

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import nam.tran.android.helper.mapper.DataMapper
import nam.tran.android.helper.model.UserModel
import nam.tran.domain.entity.state.Resource
import nam.tran.domain.interactor.app.IAppUseCase
import nam.tran.domain.interactor.user.IUserUseCase
import tran.nam.core.viewmodel.BaseFragmentViewModel
import tran.nam.util.FilePickUtils
import java.io.File
import javax.inject.Inject

class UserViewModel @Inject internal constructor(
    application: Application, val iAppUseCase: IAppUseCase,
    val dataMapper: DataMapper, val iUserUseCase: IUserUseCase
) : BaseFragmentViewModel(application) {

    val mApp = application

    var user: UserModel? = null

    val userInfo = Transformations.map(iUserUseCase.getUserInfo()) {
        dataMapper.userModelMapper.transform(it)
    }

    val updateUserInfo = MutableLiveData<Resource<*>>()

    fun logout() {
        dataMapper.preferenceMapper.transform(iAppUseCase.logout())
    }

    fun updateInfo(name: String, userModel: UserModel) {
        view<IUserView>()?.let { v ->
            val uri = user?.uri
            uri?.let {
                val path = FilePickUtils.getPath(mApp, uri) ?: ""
                iUserUseCase.updateUserInfo(userModel.id, name, File(path)).observe(v, Observer {
                    updateUserInfo.value = it
                })
            }

        }
    }
}

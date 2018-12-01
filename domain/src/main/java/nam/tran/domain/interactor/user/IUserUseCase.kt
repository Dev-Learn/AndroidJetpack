package nam.tran.domain.interactor.user

import androidx.lifecycle.LiveData
import nam.tran.domain.entity.UserEntity
import nam.tran.domain.entity.state.Resource
import java.io.File

interface IUserUseCase {
    fun getUserInfo(): LiveData<Resource<UserEntity>>
    fun updateUserInfo(id: Int, name: String, file: File): LiveData<Resource<Void>>
}
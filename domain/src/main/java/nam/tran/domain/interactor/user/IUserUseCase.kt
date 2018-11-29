package nam.tran.domain.interactor.user

import androidx.lifecycle.LiveData
import nam.tran.domain.entity.UserEntity
import nam.tran.domain.entity.state.Resource

interface IUserUseCase {
    fun getUserInfo(): LiveData<Resource<UserEntity>>
}
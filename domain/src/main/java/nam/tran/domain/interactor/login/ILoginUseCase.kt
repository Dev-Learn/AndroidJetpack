package nam.tran.domain.interactor.login

import androidx.lifecycle.LiveData
import nam.tran.domain.entity.state.Resource

interface ILoginUseCase {
    fun login(email : String,password:String): LiveData<Resource<Void>>
}
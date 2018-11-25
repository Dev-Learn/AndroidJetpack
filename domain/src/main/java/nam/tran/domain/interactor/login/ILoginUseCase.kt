package nam.tran.domain.interactor.login

import androidx.lifecycle.LiveData
import nam.tran.domain.entity.state.Resource

interface ILoginUseCase {
    fun login(email: String, password: String): LiveData<Resource<Void>>
    fun register(name: String, email: String, password: String): LiveData<Resource<String>>
    fun forgotPassword(email: String): LiveData<Resource<String>>
    fun send_email_verify(email: String, password: String): LiveData<Resource<Void>>
}
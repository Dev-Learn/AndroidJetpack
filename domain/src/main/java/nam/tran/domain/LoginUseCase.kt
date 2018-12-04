package nam.tran.domain

import androidx.lifecycle.LiveData
import nam.tran.domain.entity.state.Loading
import nam.tran.domain.entity.state.Resource
import nam.tran.domain.executor.AppExecutors
import nam.tran.domain.interactor.core.DataBoundNetwork
import nam.tran.domain.interactor.login.ILoginUseCase
import nam.tran.flatform.IApi
import nam.tran.flatform.local.IPreference
import nam.tran.flatform.model.request.EmailVerifyRequest
import nam.tran.flatform.model.request.LoginRequest
import nam.tran.flatform.model.request.RegisterRequest
import retrofit2.Call
import javax.inject.Inject


class LoginUseCase @Inject
internal constructor(
    private val appExecutors: AppExecutors,
    private val iApi: IApi,
    private val iPreference: IPreference
) : ILoginUseCase {

    override fun login(email: String, password: String): LiveData<Resource<Void>> {
        return object : DataBoundNetwork<Void, String>(appExecutors) {
            override fun convertData(body: String?): Void? {
                iPreference.saveToken(body)
                return null
            }

            override fun statusLoading(): Int {
                return Loading.LOADING_DIALOG
            }

            override fun createCall(): Call<String> {
                return iApi.login(LoginRequest(email, password))
            }

        }.asLiveData()
    }

    override fun register(name: String, email: String, password: String): LiveData<Resource<String>> {
        return object : DataBoundNetwork<String, String>(appExecutors) {
            override fun convertData(body: String?): String? {
                return body
            }

            override fun statusLoading(): Int {
                return Loading.LOADING_DIALOG
            }

            override fun createCall(): Call<String> {
                return iApi.register(RegisterRequest(name, email, password))
            }

        }.asLiveData()
    }

    override fun forgotPassword(email: String): LiveData<Resource<String>> {
        return object : DataBoundNetwork<String, String>(appExecutors) {
            override fun convertData(body: String?): String? {
                return body
            }

            override fun statusLoading(): Int {
                return Loading.LOADING_DIALOG
            }

            override fun createCall(): Call<String> {
                return iApi.forgotPassword(email)
            }

        }.asLiveData()
    }

    override fun send_email_verify(email: String, password: String): LiveData<Resource<Void>> {
        return object : DataBoundNetwork<Void, Void>(appExecutors) {
            override fun convertData(body: Void?): Void? {
                return null
            }

            override fun statusLoading(): Int {
                return Loading.LOADING_DIALOG
            }

            override fun createCall(): Call<Void> {
                return iApi.sendEmailVerify(EmailVerifyRequest(email, password))
            }

        }.asLiveData()
    }
}
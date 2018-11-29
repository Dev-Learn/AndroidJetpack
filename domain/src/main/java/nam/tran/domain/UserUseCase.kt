package nam.tran.domain

import androidx.lifecycle.LiveData
import nam.tran.domain.entity.UserEntity
import nam.tran.domain.entity.state.Loading
import nam.tran.domain.entity.state.Resource
import nam.tran.domain.executor.AppExecutors
import nam.tran.domain.interactor.core.DataBoundNetwork
import nam.tran.domain.interactor.user.IUserUseCase
import nam.tran.domain.mapper.DataEntityMapper
import nam.tran.flatform.IApi
import nam.tran.flatform.model.response.User
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File
import javax.inject.Inject

class UserUseCase @Inject
internal constructor(
    private val appExecutors: AppExecutors,
    private val iApi: IApi,
    private val dataEntityMapper: DataEntityMapper
) : IUserUseCase {

    override fun getUserInfo(): LiveData<Resource<UserEntity>> =
        object : DataBoundNetwork<UserEntity, User>(appExecutors) {
            override fun convertData(body: User?): UserEntity? {
                return dataEntityMapper.userEntityMapper.transform(body)
            }

            override fun statusLoading(): Int {
                return Loading.LOADING_NORMAL
            }

            override fun createCall(): Call<User> {
                return iApi.getUserInfo()
            }

        }.asLiveData()

    override fun updateUserInfo(id: Int, name: String, file: File, fileType: String?) =
        object : DataBoundNetwork<UserEntity, Void>(appExecutors) {
            override fun convertData(body: Void?): UserEntity? {
                return null
            }

            override fun statusLoading(): Int {
                return Loading.LOADING_DIALOG
            }

            override fun createCall(): Call<Void> {

                // add another part within the multipart request
                val idRequest = RequestBody.create(
                    okhttp3.MultipartBody.FORM, id.toString()
                )

                // add another part within the multipart request
                val nameRequest = RequestBody.create(
                    okhttp3.MultipartBody.FORM, name
                )

                // create RequestBody instance from file
                val requestFile = RequestBody.create(
                    MediaType.parse(fileType),
                    file
                )

                // MultipartBody.Part is used to send also the actual file name
                val body = MultipartBody.Part.createFormData("picture", file.name, requestFile)


                return iApi.updateUserInfo(idRequest, nameRequest, body)
            }

        }.asLiveData()
}
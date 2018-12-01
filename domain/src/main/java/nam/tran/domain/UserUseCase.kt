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
import tran.nam.util.createPartFromString
import tran.nam.util.prepareFilePart
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

    override fun updateUserInfo(id: Int, name: String, file: File) =
        object : DataBoundNetwork<Void, Void>(appExecutors) {
            override fun convertData(body: Void?): Void? {
                return null
            }

            override fun statusLoading(): Int {
                return Loading.LOADING_DIALOG
            }

            override fun createCall(): Call<Void> {
                return iApi.updateUserInfo(
                    createPartFromString("id", id.toString()),
                    createPartFromString("name", name),
                    prepareFilePart("picture", file)
                )
            }

        }.asLiveData()
}
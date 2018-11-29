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
import retrofit2.Call
import javax.inject.Inject

class UserUseCase @Inject
internal constructor(
    private val appExecutors: AppExecutors,
    private val iApi: IApi,
    private val dataEntityMapper: DataEntityMapper
) : IUserUseCase {

    override fun getUserInfo(): LiveData<Resource<UserEntity>> {
        return object : DataBoundNetwork<UserEntity, User>(appExecutors) {
            override fun convertData(body: User?): UserEntity? {
                return dataEntityMapper.userEntityMapper.transform(body)
            }

            override fun statusLoading(): Int {
                return Loading.LOADING_DIALOG
            }

            override fun createCall(): Call<User> {
                return iApi.getUserInfo()
            }

        }.asLiveData()
    }

}
package nam.tran.domain

//import nam.tran.flatform.database.DbProvider
import androidx.lifecycle.LiveData
import nam.tran.domain.entity.ComicEntity
import nam.tran.domain.entity.state.Resource
import nam.tran.domain.executor.AppExecutors
import nam.tran.domain.interactor.core.DataBoundNetwork
import nam.tran.domain.mapper.DataEntityMapper
import nam.tran.flatform.IApi
import nam.tran.flatform.core.ApiResponse
import nam.tran.flatform.local.IPreference
import nam.tran.flatform.model.response.ComicResponse
import javax.inject.Inject

@Suppress("unused")
class Repository @Inject
internal constructor(
    private val appExecutors: AppExecutors,
    private val iPreference: IPreference,
    private val dataEntityMapper: DataEntityMapper,
    private val iApi: IApi
    /*, private val dbProvider: DbProvider*/
) : IRepository {

    override fun getComic(offset: Int, count: Int, typeLoading: Int): LiveData<Resource<List<ComicEntity>>> {
        return object : DataBoundNetwork<List<ComicEntity>, ComicResponse>(appExecutors) {
            override fun convertData(body: ComicResponse?): List<ComicEntity>? {
                return dataEntityMapper.comicEntityMapper.transform(body?.result)
            }

            override fun statusLoading(): Int {
                return typeLoading
            }

            override fun createCall(): LiveData<ApiResponse<ComicResponse>> {
                return iApi.getComic(offset, count)
            }

        }.asLiveData()
    }


}

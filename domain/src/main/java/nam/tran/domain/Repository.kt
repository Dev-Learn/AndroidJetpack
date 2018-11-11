package nam.tran.domain

//import nam.tran.flatform.database.DbProvider
import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList
import nam.tran.domain.entity.ComicEntity
import nam.tran.domain.entity.state.Resource
import nam.tran.domain.executor.AppExecutors
import nam.tran.domain.interactor.DataSourceFactory
import nam.tran.domain.mapper.DataEntityMapper
import nam.tran.flatform.IApi
import nam.tran.flatform.core.ApiResponse
import nam.tran.flatform.local.IPreference
import nam.tran.flatform.model.response.Comic
import javax.inject.Inject

class Repository @Inject
internal constructor(
    private val appExecutors: AppExecutors,
    private val iPreference: IPreference,
    private val dataEntityMapper: DataEntityMapper,
    private val iApi: IApi
    /*, private val dbProvider: DbProvider*/
) : IRepository {

    override fun getComic(offset: Int, count: Int, typeLoading: Int): LiveData<Resource<PagedList<ComicEntity>>> {
        val sourceFactory =  object : DataSourceFactory<Void, ComicEntity, Comic>(appExecutors) {
            override fun convertData(body: Comic?): ComicEntity? {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getRequest(item: Comic): Void {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun statusLoading(): Int {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun createCall(): LiveData<ApiResponse<Comic>> {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }

        sourceFactory.toLiveas()
    }


}

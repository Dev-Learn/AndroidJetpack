package nam.tran.domain.interactor

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import nam.tran.domain.entity.ComicEntity
import nam.tran.domain.mapper.DataEntityMapper
import nam.tran.flatform.IApi
import tran.nam.util.Logger
import java.util.concurrent.Executor

class PageDataSourceFactory(
    private val iApi: IApi,
    private val dataEntityMapper: DataEntityMapper,
    private val retryExecutor: Executor,
    private val convert: (List<ComicEntity>) -> List<Any>
) : DataSource.Factory<Int, Any>() {

    val sourceLiveData = MutableLiveData<PageKeyedComicDataSource>()

    override fun create(): DataSource<Int, Any> {
        Logger.debug("Paging Learn","PageDataSourceFactory - create()")
        val source = PageKeyedComicDataSource(iApi, dataEntityMapper, retryExecutor,convert)
        sourceLiveData.postValue(source)
        return source
    }

}
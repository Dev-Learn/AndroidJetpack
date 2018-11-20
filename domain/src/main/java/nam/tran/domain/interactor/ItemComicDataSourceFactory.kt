package nam.tran.domain.interactor

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import nam.tran.flatform.model.response.BaseItemKey
import nam.tran.domain.entity.ComicEntity
import nam.tran.domain.mapper.DataEntityMapper
import nam.tran.flatform.IApi
import nam.tran.flatform.database.DbProvider
import tran.nam.util.Logger
import java.util.concurrent.Executor

class ItemComicDataSourceFactory(
    private val iApi: IApi,
    private val dataEntityMapper: DataEntityMapper,
    private val dbProvider: DbProvider,
    private val appExecutors: Executor,
    private val convert: (List<ComicEntity>) -> List<BaseItemKey>
) : DataSource.Factory<Int, BaseItemKey>() {

    val sourceLiveData = MutableLiveData<ItemKeyedComicDataSource>()

    override fun create(): DataSource<Int, BaseItemKey> {
        Logger.debug("Paging Learn", "PageDataSourceFactory - create()")
        val source = ItemKeyedComicDataSource(iApi, dataEntityMapper, dbProvider, appExecutors, convert)
        sourceLiveData.postValue(source)
        return source
    }

}
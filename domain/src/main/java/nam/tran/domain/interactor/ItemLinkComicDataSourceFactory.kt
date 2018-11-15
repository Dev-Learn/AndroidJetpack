package nam.tran.domain.interactor

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import nam.tran.domain.entity.BaseItemKey
import nam.tran.domain.entity.LinkComicEntity
import nam.tran.domain.mapper.DataEntityMapper
import nam.tran.flatform.IApi
import tran.nam.util.Logger
import java.util.concurrent.Executor

class ItemLinkComicDataSourceFactory(
    private val idComic: Int,
    private val iApi: IApi,
    private val dataEntityMapper: DataEntityMapper,
    private val retryExecutor: Executor,
    private val convert: (List<LinkComicEntity>) -> List<BaseItemKey>
) : DataSource.Factory<Int, BaseItemKey>() {

    val sourceLiveData = MutableLiveData<ItemKeyedComicLinkDataSource>()

    override fun create(): DataSource<Int, BaseItemKey> {
        Logger.debug("Paging Learn", "PageDataSourceFactory - create()")
        val source = ItemKeyedComicLinkDataSource(idComic, iApi, dataEntityMapper, retryExecutor, convert)
        sourceLiveData.postValue(source)
        return source
    }

}
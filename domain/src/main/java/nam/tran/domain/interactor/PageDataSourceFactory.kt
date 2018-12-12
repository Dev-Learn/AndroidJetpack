package nam.tran.domain.interactor

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import nam.tran.domain.entity.ComicEntity
import nam.tran.domain.entity.core.BaseItemKey
import nam.tran.domain.mapper.DataEntityMapper
import nam.tran.flatform.IApi
import tran.nam.util.Logger

class PageDataSourceFactory(
    private val iApi: IApi,
    private val dataEntityMapper: DataEntityMapper,
    private val convert: (List<ComicEntity>) -> List<BaseItemKey>
) : DataSource.Factory<Int, BaseItemKey>() {

    val sourceLiveData = MutableLiveData<PageKeyedComicDataSource>()

    override fun create(): DataSource<Int, BaseItemKey> {
        Logger.debug("Paging Learn", "PageDataSourceFactory - create()")
        val source = PageKeyedComicDataSource(iApi, dataEntityMapper, convert)
        sourceLiveData.postValue(source)
        return source
    }

}
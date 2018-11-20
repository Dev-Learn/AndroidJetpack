package nam.tran.domain.interactor

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import nam.tran.flatform.model.response.BaseItemKey
import nam.tran.domain.entity.LinkComicEntity
import nam.tran.domain.mapper.DataEntityMapper
import nam.tran.flatform.IApi
import tran.nam.util.Logger

class ItemLinkComicDataSourceFactory(
    private val idComic: Int,
    private val iApi: IApi,
    private val dataEntityMapper: DataEntityMapper,
    private val convert: (List<LinkComicEntity>) -> List<BaseItemKey>
) : DataSource.Factory<Int, BaseItemKey>() {

    val sourceLiveData = MutableLiveData<ItemKeyedComicLinkDataSource>()

    override fun create(): DataSource<Int, BaseItemKey> {
        Logger.debug("Paging Learn", "PageDataSourceFactory - create()")
        val source = ItemKeyedComicLinkDataSource(idComic, iApi, dataEntityMapper, convert)
        sourceLiveData.postValue(source)
        return source
    }

}
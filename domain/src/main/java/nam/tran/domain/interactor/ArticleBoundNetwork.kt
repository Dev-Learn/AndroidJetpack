package nam.tran.domain.interactor

import androidx.lifecycle.MutableLiveData
import nam.tran.domain.entity.ArticleEntity
import nam.tran.domain.entity.state.Loading
import nam.tran.domain.executor.AppExecutors
import nam.tran.domain.interactor.core.DataBoundNetwork
import nam.tran.domain.mapper.DataEntityMapper
import nam.tran.flatform.IApi
import nam.tran.flatform.model.response.Article
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleBoundNetwork @Inject constructor(
    appExecutors: AppExecutors, private val dataEntityMapper: DataEntityMapper,
    private val iApi: IApi
) : DataBoundNetwork<List<Article>,List<ArticleEntity>>(appExecutors) {

    override fun convertData(body: List<Article>?): List<ArticleEntity>? {
        return dataEntityMapper.articleEntityMapper.transform(body)
    }

    override fun statusLoading(): Int {
        return Loading.LOADING_NORMAL
    }

    override fun createCall(): Call<List<Article>> {
        return iApi.getArticle()
    }
}
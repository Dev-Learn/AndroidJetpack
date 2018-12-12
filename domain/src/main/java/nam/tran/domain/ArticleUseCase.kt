package nam.tran.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nam.tran.domain.entity.ArticleEntity
import nam.tran.domain.entity.ItemKeyArticle
import nam.tran.domain.entity.state.Listing
import nam.tran.domain.executor.AppExecutors
import nam.tran.domain.interactor.ArticleBoundNetwork
import nam.tran.domain.interactor.article.IArticleUseCase
import nam.tran.domain.mapper.DataEntityMapper
import nam.tran.flatform.IApi
import javax.inject.Inject

class ArticleUseCase @Inject
internal constructor(
    private val appExecutors: AppExecutors,
    private val dataEntityMapper: DataEntityMapper,
    private val iApi: IApi
) : IArticleUseCase {

    private lateinit var articleBoundNetwork: ArticleBoundNetwork

    override fun loadInitial(
        limit: Int,
        convert: (List<ArticleEntity>) -> List<ItemKeyArticle<String>>
    ): LiveData<Listing<ArrayList<ItemKeyArticle<String>>>> {
        articleBoundNetwork = ArticleBoundNetwork(appExecutors, dataEntityMapper, iApi)
        articleBoundNetwork.initial(convert)
        val result = MutableLiveData<Listing<ArrayList<ItemKeyArticle<String>>>>()
        result.postValue(Listing(articleBoundNetwork.result, articleBoundNetwork.networkState))
        return result
    }

    override fun loadAfter(after: Int, convert: (ArticleEntity) -> ItemKeyArticle<String>) {
        articleBoundNetwork.loadAfter(after, convert)
    }

    override fun loadBefore(before: Int, convert: (ArticleEntity) -> ItemKeyArticle<String>) {
        articleBoundNetwork.loadBefore(before, convert)
    }
}
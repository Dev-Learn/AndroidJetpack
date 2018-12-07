package nam.tran.domain

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import nam.tran.domain.entity.ArticleEntity
import nam.tran.domain.entity.state.Resource
import nam.tran.domain.executor.AppExecutors
import nam.tran.domain.interactor.ArticleBoundNetwork
import nam.tran.domain.interactor.article.IArticleUseCase
import nam.tran.domain.mapper.DataEntityMapper
import nam.tran.flatform.IApi
import javax.inject.Inject

class ArticleUseCase @Inject
internal constructor(
    private val appExecutors: AppExecutors, private val dataEntityMapper: DataEntityMapper,
    private val iApi: IApi
) : IArticleUseCase {

    private lateinit var articleBoundNetwork: ArticleBoundNetwork

    override fun loadInitial(limit: Int) {
        articleBoundNetwork = ArticleBoundNetwork(appExecutors, dataEntityMapper, iApi)
        articleBoundNetwork.fetchFromNetwork()
    }

    override fun loadAfter(after: Int) {
        articleBoundNetwork.id = after
        articleBoundNetwork.isAfter = true
        articleBoundNetwork.fetchFromNetwork()
    }

    override fun loadBefore(before: Int) {
        articleBoundNetwork.id = before
        articleBoundNetwork.isAfter = false
        articleBoundNetwork.fetchFromNetwork()
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<Resource<List<ArticleEntity>>>) {
        articleBoundNetwork.result.observe(owner, observer)
    }

    override fun unObserve(observer: Observer<Resource<List<ArticleEntity>>>) {
        articleBoundNetwork.result.removeObserver(observer)
    }
}
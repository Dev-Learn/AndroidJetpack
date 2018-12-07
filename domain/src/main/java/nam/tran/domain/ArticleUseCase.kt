package nam.tran.domain

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import nam.tran.domain.entity.ArticleEntity
import nam.tran.domain.entity.state.Resource
import nam.tran.domain.interactor.ArticleBoundNetwork
import nam.tran.domain.interactor.article.IArticleUseCase
import javax.inject.Inject

class ArticleUseCase @Inject
internal constructor(
    private val articleBoundNetwork: ArticleBoundNetwork
) : IArticleUseCase {

    override fun loadInitial(limit: Int) {
        articleBoundNetwork.id = null
        articleBoundNetwork.isAfter = true
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
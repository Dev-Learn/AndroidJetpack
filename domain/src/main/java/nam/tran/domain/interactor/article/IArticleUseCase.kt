package nam.tran.domain.interactor.article

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import nam.tran.domain.entity.ArticleEntity
import nam.tran.domain.entity.state.Resource

interface IArticleUseCase {
    fun loadInitial(limit: Int = 30)
    fun observe(owner: LifecycleOwner, observer: Observer<Resource<List<ArticleEntity>>>)
    fun unObserve(observer: Observer<Resource<List<ArticleEntity>>>)
    fun loadAfter(after: Int)
    fun loadBefore(before: Int)
}
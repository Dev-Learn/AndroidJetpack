package nam.tran.android.helper.view.home.article.viewmodel;

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import nam.tran.android.helper.mapper.DataMapper
import nam.tran.android.helper.model.ArticleModel
import nam.tran.domain.entity.ArticleEntity
import nam.tran.domain.entity.state.Resource
import nam.tran.domain.interactor.article.IArticleUseCase
import tran.nam.core.viewmodel.BaseFragmentViewModel
import tran.nam.core.viewmodel.IProgressViewModel
import javax.inject.Inject

class ArticleViewModel @Inject internal constructor(
    application: Application,
    val dataMapper: DataMapper,
    private val iArticleUseCase: IArticleUseCase
) : BaseFragmentViewModel(application),
    IProgressViewModel, Observer<Resource<List<ArticleEntity>>> {

    init {
        iArticleUseCase.loadInitial()
    }

    val results = MutableLiveData<Resource<List<ArticleModel>>>()

    override fun onCreated() {
        view<IArticleView>()?.let {
            iArticleUseCase.observe(it, this)
        }
    }

    override fun resource(): Resource<List<ArticleModel>>? {
        return results.value
    }

    override fun onChanged(it: Resource<List<ArticleEntity>>) {
        results.postValue(dataMapper.articleModelMapper.transform(it))
    }

    fun loadMore(article: ArticleModel?) {
        article?.let { item ->
            iArticleUseCase.loadAfter(item.id)
        }
    }

    fun loadBefore(article: ArticleModel?) {
        article?.let { item ->
            iArticleUseCase.loadBefore(item.id)
        }
    }

    override fun onCleared() {
        iArticleUseCase.unObserve(this)
    }
}

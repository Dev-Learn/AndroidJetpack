package nam.tran.android.helper.view.home.article.viewmodel

import android.app.Application
import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import nam.tran.android.helper.mapper.DataMapper
import nam.tran.android.helper.model.ArticleModel
import nam.tran.domain.entity.state.Resource
import nam.tran.domain.interactor.article.IArticleUseCase
import nam.tran.flatform.model.response.BaseItemKey
import tran.nam.core.viewmodel.BaseFragmentViewModel
import tran.nam.core.viewmodel.IProgressViewModel
import tran.nam.util.Logger
import javax.inject.Inject

class ArticleViewModel @Inject internal constructor(
    application: Application,
    val dataMapper: DataMapper,
    private val iArticleUseCase: IArticleUseCase
) : BaseFragmentViewModel(application),
    IProgressViewModel {

    private val response = iArticleUseCase.loadInitial {
        dataMapper.articleModelMapper.transform(it)
    }

    val data: LiveData<ArrayList<BaseItemKey>> = Transformations.switchMap(response) {
        it.pagedList
    }

    val results: LiveData<Resource<*>> = Transformations.switchMap(response) {
        it.networkState
    }

    override fun resource(): Resource<*>? {
        return results.value
    }

    fun loadMore(article: ArticleModel?) {
        article?.let { item ->
            Logger.debug("""getItemEnd : ${item.id}""")
            iArticleUseCase.loadAfter(item.id) {
                dataMapper.articleModelMapper.transform(it)
            }
        }
    }

    fun loadBefore(article: ArticleModel?) {
        article?.let { item ->
            Logger.debug("""getItemFirst : ${item.id}""")
            iArticleUseCase.loadBefore(item.id) {
                dataMapper.articleModelMapper.transform(it)
            }
        }
    }
}

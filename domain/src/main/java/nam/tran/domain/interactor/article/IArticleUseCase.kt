package nam.tran.domain.interactor.article

import androidx.lifecycle.LiveData
import nam.tran.domain.entity.ArticleEntity
import nam.tran.domain.entity.state.Listing
import nam.tran.flatform.model.response.BaseItemKey

interface IArticleUseCase {
    fun loadInitial(limit: Int = 30,convert: (List<ArticleEntity>) -> List<BaseItemKey>): LiveData<Listing<ArrayList<BaseItemKey>>>
    fun loadAfter(after: Int, convert: (List<ArticleEntity>) -> List<BaseItemKey>)
    fun loadBefore(before: Int, convert: (List<ArticleEntity>) -> List<BaseItemKey>)
}
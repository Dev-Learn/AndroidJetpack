package nam.tran.domain.interactor.article

import androidx.lifecycle.LiveData
import nam.tran.domain.entity.ArticleEntity
import nam.tran.domain.entity.ItemKeyArticle
import nam.tran.domain.entity.state.Listing

interface IArticleUseCase {
    fun loadInitial(
        limit: Int = 30,
        convert: (List<ArticleEntity>) -> List<ItemKeyArticle<String>>
    ): LiveData<Listing<ArrayList<ItemKeyArticle<String>>>>

    fun loadAfter(after: Int, convert: (ArticleEntity) -> ItemKeyArticle<String>)
    fun loadBefore(before: Int, convert: (ArticleEntity) -> ItemKeyArticle<String>)
}
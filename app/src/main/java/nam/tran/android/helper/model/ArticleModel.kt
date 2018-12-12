package nam.tran.android.helper.model

import nam.tran.domain.entity.core.ItemKeyArticle

data class ArticleModel(
    val id: Int,
    val title: String = "",
    val image: String = "",
    val description: String = "",
    val time_ago: String = "",
    val author: AuthorArticleModel? = null,
    val detailArticle: DetailArticleModel? = null,
    val isheader: Boolean, val value: String
) : ItemKeyArticle<String>(id,isheader,value) {

    fun title(): String {
        return id.toString() + " : " + title
    }
}
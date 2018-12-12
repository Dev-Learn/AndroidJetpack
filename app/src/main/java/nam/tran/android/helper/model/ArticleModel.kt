package nam.tran.android.helper.model

import nam.tran.domain.entity.ItemKeyArticle

data class ArticleModel(
    val id: Int = -1,
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

    companion object {
        fun mock():ArticleModel{
            return ArticleModel(isheader = false,value = "")
        }
    }
}
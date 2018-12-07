package nam.tran.android.helper.model

data class ArticleModel(
    val id: Int,
    val title: String = "",
    val image: String = "",
    val description: String = "",
    val time_ago: String = "",
    val author: AuthorArticleModel? = null,
    val detailArticle: DetailArticleModel? = null
) {
    fun title(): String {
        return id.toString() + " : " + title
    }

    companion object {
        fun mock(id : Int):ArticleModel{
            return ArticleModel(id = id)
        }
    }
}
package nam.tran.android.helper.model

data class ArticleModel(
    val id: Int,
    val title: String,
    val image: String,
    val description: String,
    val time_ago: String,
    val author: AuthorArticleModel?,
    val detailArticle: DetailArticleModel?
)
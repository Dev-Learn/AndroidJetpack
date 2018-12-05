package nam.tran.domain.entity

data class ArticleEntity(
    val id: Int,
    val title: String,
    val image: String,
    val description: String,
    val time_ago: String,
    val author: AuthorArticleEntity?,
    val detailArticle: DetailArticleEntity?
)
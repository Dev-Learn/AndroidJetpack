package nam.tran.flatform.model.response

data class Article(
    val id: Int,
    val title: String,
    val image: String,
    val description: String,
    val time_ago: String,
    val author: AuthorArticle?,
    val detailArticle: DetailArticle?
)
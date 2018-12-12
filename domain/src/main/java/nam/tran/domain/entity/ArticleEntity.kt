package nam.tran.domain.entity

import android.annotation.SuppressLint
import nam.tran.domain.entity.core.IHeader
import java.text.SimpleDateFormat

data class ArticleEntity(
    val id: Int = -1,
    val title: String = "",
    val image: String = "",
    val description: String = "",
    val time_ago: String = "",
    val author: AuthorArticleEntity? = null,
    val detailArticle: DetailArticleEntity? = null,
    override val isHeader: Boolean = false,
    override val headerValue: String
) : IHeader<String> {

    companion object {
        fun header(id: Int, headerValue: String): ArticleEntity {
            return ArticleEntity(id = id, isHeader = true, headerValue = headerValue)
        }
    }
}
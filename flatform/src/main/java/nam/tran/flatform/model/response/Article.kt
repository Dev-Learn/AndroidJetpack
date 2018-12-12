package nam.tran.flatform.model.response

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

data class Article(
    val id: Int,
    val title: String,
    val image: String,
    val description: String,
    val time_ago: String,
    val author: AuthorArticle?,
    val detailArticle: DetailArticle?
) {
    @SuppressLint("SimpleDateFormat")
    fun day(): String {
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time_ago)
        return SimpleDateFormat("dd/MM/yyyy").format(date)
    }
}
package nam.tran.android.helper.model

import nam.tran.domain.entity.core.BaseItemKey
import java.io.Serializable

data class ComicModel(
    var id: Int,
    var title: String,
    var description: String,
    var image: String,
    var genre: ArrayList<GenreModel>
) : BaseItemKey(id), Serializable {

    var isLike = false

    val listGenre: String
        get() {
            var data: String = ""
            for ((index, value) in genre.withIndex()) {
                if (index == genre.size - 1)
                    data += value.genre
                else
                    data += (value.genre.plus(", "))
            }
            return data
        }

    override fun toString(): String {
        return "ComicModel(id=$id, title='$title', description='$description', image='$image', genre=$genre)"
    }


}
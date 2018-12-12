package nam.tran.domain.entity

import nam.tran.domain.entity.core.BaseItemKey

data class ComicEntity(
    val id: Int,
    var title: String,
    var description: String,
    var image: String,
    var genre: ArrayList<GenreEntity>
) : BaseItemKey(id) {
    var isLike = false

    override fun equals(other: Any?): Boolean {
        return other is ComicEntity && other.id == id
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + image.hashCode()
        result = 31 * result + genre.hashCode()
        result = 31 * result + isLike.hashCode()
        return result
    }
}
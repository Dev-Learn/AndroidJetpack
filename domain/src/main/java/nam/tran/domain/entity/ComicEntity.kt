package nam.tran.domain.entity

import nam.tran.flatform.model.response.BaseItemKey

data class ComicEntity(
    val id: Int,
    var title: String,
    var description: String,
    var image: String,
    var genre: ArrayList<GenreEntity>
) : BaseItemKey(id){
    var isLike = false

    override fun equals(other: Any?): Boolean {
        return other is ComicEntity && other.id == id
    }
}
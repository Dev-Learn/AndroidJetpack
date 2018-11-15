package nam.tran.domain.entity

data class ComicEntity(
    val id: Int,
    var title: String,
    var description: String,
    var image: String,
    var genre: ArrayList<GenreEntity>
) : BaseItemKey(id)
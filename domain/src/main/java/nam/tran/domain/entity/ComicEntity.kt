package nam.tran.domain.entity

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
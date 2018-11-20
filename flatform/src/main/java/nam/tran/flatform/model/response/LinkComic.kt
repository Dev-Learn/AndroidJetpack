package nam.tran.flatform.model.response

import androidx.room.Entity

@Entity(tableName = "ComicImage", primaryKeys = ["id"])
class LinkComic(var id: Int, var idcomic: Int, var image: String)
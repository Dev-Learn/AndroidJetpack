package nam.tran.android.helper.model

import java.io.Serializable

data class GenreModel(var genre: String) : Serializable {
    override fun toString(): String {
        return "GenreModel(genre='$genre')"
    }
}
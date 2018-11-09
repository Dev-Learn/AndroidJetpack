package nam.tran.android.helper.model

data class ComicModel(var id : Int, var title : String, var description : String, var image : String, var genre : ArrayList<GenreModel>){

    val listGenre: String
        get() {
            var data : String = ""
            for ((index, value) in genre.withIndex()){
                if (index == genre.size - 1)
                    data += value.genre
                else
                    data += (value.genre.plus(", "))
            }
            return data
        }
}
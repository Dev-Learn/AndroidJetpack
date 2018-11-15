package nam.tran.flatform.model.response

data class LinkComicResponse(val success: Boolean, val result: ArrayList<LinkComic>, val message : String? = null)
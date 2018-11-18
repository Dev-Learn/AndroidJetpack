package nam.tran.flatform.model.response

data class ComicResponse(val success: Boolean, val result: List<Comic>, val message: String? = null)
package nam.tran.flatform

import androidx.lifecycle.LiveData
import nam.tran.flatform.core.ApiResponse
import nam.tran.flatform.model.response.Comic
import nam.tran.flatform.model.response.ComicResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface IApi {

    @GET("/getComic")
    fun getComic(@Query("offset") offset: Int,@Query("count") count: Int): LiveData<ApiResponse<ComicResponse>>

}

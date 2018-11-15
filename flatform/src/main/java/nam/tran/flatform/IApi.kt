package nam.tran.flatform

import androidx.lifecycle.LiveData
import nam.tran.flatform.core.ApiResponse
import nam.tran.flatform.model.response.ComicResponse
import nam.tran.flatform.model.response.LinkComicResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface IApi {

    @GET("/getComicOffset")
    fun getComic(@Query("offset") offset: Int, @Query("count") count: Int): LiveData<ApiResponse<ComicResponse>>

    @GET("/getComicOffset")
    fun getComicPaging(@Query("offset") offset: Int, @Query("count") count: Int): Call<ComicResponse>

    @GET("/getComic")
    fun getComicPaging2(@Query("after") after: Int? = null, @Query("limit") limit: Int): Call<ComicResponse>

    @POST("/getComicImage")
    fun getLinkComicPaging(@Query("after") after: Int? = null, @Query("limit") limit: Int, @Body idComic: Int): Call<LinkComicResponse>

}

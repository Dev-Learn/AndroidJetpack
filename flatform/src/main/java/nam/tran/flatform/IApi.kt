package nam.tran.flatform

import androidx.lifecycle.LiveData
import nam.tran.flatform.model.request.LoginRequest
import nam.tran.flatform.model.response.Comic
import nam.tran.flatform.model.response.LinkComic
import retrofit2.Call
import retrofit2.http.*

interface IApi {

    @GET("/getComicOffset")
    fun getComic(@Query("offset") offset: Int, @Query("count") count: Int): Call<List<Comic>>

    @GET("/getComicOffset")
    fun getComicPaging(@Query("offset") offset: Int, @Query("count") count: Int): Call<List<Comic>>

    @GET("/getComic")
    fun getComicPaging2(@Query("after") after: Int? = null, @Query("limit") limit: Int): Call<List<Comic>>

    @GET("/getComicImage/{id}")
    fun getLinkComicPaging(@Path("id") id: Int, @Query("after") after: Int? = null, @Query("limit") limit: Int): Call<List<LinkComic>>

    @POST("/login")
    fun login(@Body loginRequest: LoginRequest): Call<String>
}

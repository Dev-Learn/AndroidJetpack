package nam.tran.flatform

import nam.tran.flatform.model.request.EmailVerifyRequest
import nam.tran.flatform.model.request.LoginRequest
import nam.tran.flatform.model.request.RegisterRequest
import nam.tran.flatform.model.response.Article
import nam.tran.flatform.model.response.Comic
import nam.tran.flatform.model.response.LinkComic
import nam.tran.flatform.model.response.User
import okhttp3.MultipartBody
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

    @PUT("/register")
    fun register(@Body registerRequest: RegisterRequest): Call<String>

    @POST("/resetPassword")
    fun forgotPassword(@Body email: String): Call<String>

    @POST("/sendEmailVerify")
    fun sendEmailVerify(@Body emailVerifyRequest: EmailVerifyRequest): Call<Void>

    @POST("/userInfo")
    fun getUserInfo(): Call<User>

    @Multipart
    @POST("/updateInfo")
    fun updateUserInfo(
        @Part id: MultipartBody.Part,
        @Part name: MultipartBody.Part,
        @Part file: MultipartBody.Part
    ): Call<Void>

    @GET
    fun getArticle(
        @Url url: String = "http://192.168.7.152:5000/getArticle", @Query("before") before: Int? = null, @Query("after") after: Int? = null, @Query(
            "limit"
        ) limit: Int = 30
    ): Call<List<Article>>
}

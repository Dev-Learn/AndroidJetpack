package nam.tran.flatform

import android.arch.lifecycle.LiveData
import nam.tran.flatform.core.ApiResponse
import nam.tran.flatform.model.response.Comic
import retrofit2.http.GET
import retrofit2.http.Query

interface IApi {

    @GET("/getComic")
    fun getComic(@Query("offset") offset: Int,@Query("count") count: Int): LiveData<ApiResponse<List<Comic>>>

}

package nam.tran.domain.interactor

import androidx.lifecycle.MutableLiveData
import nam.tran.domain.entity.ArticleEntity
import nam.tran.domain.entity.state.ErrorResource
import nam.tran.domain.entity.state.Resource
import nam.tran.domain.mapper.DataEntityMapper
import nam.tran.flatform.IApi
import nam.tran.flatform.model.response.Article
import nam.tran.flatform.model.response.BaseItemKey
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tran.nam.util.Constant.Companion.LIMIT

class ArticleBoundNetwork constructor(
    private val dataEntityMapper: DataEntityMapper,
    private val iApi: IApi
) {

    val result = MutableLiveData<ArrayList<BaseItemKey>>()
    val networkState = MutableLiveData<Resource<*>>()

    fun initial(convert: (List<ArticleEntity>) -> List<BaseItemKey>) {
        networkState.postValue(
            Resource.loading(
                null
            )
        )
        iApi.getArticle(limit = 50).enqueue(object : Callback<List<Article>> {
            override fun onFailure(call: Call<List<Article>>, t: Throwable) {
                networkState.postValue(
                    Resource.error(
                        ErrorResource(massage = t.message ?: "unknown err"),
                        null,
                        retry = {
                            initial(convert)
                        })
                )
            }

            override fun onResponse(call: Call<List<Article>>, response: Response<List<Article>>) {
                if (response.isSuccessful) {
                    networkState.postValue(Resource.success(null))
                    result.postValue(ArrayList(convert(dataEntityMapper.articleEntityMapper.transform(response.body()))))
                } else {
                    networkState.postValue(
                        Resource.error(
                            ErrorResource(
                                JSONObject(response.errorBody()?.string()).getString("message"),
                                response.code()
                            ),
                            null,
                            retry = {
                                initial(convert)
                            })
                    )
                }
            }
        })
    }

    fun loadAfter(after: Int, convert: (List<ArticleEntity>) -> List<BaseItemKey>) {
        networkState.postValue(
            Resource.loadingPaging(
                null
            )
        )
        iApi.getArticle(after = after).enqueue(object : Callback<List<Article>> {
            override fun onFailure(call: Call<List<Article>>, t: Throwable) {
                networkState.postValue(
                    Resource.errorPaging(
                        ErrorResource(massage = t.message ?: "unknown err"),
                        null,
                        retry = {
                            loadAfter(after, convert)
                        })
                )
            }

            override fun onResponse(call: Call<List<Article>>, response: Response<List<Article>>) {
                if (response.isSuccessful) {
                    val resultRespo = response.body()
                    if (resultRespo?.isEmpty()!!) {
                        networkState.postValue(Resource.successPaging(1))
                    } else {
                        networkState.postValue(Resource.successPaging(null))
                        val data = result.value
                        data?.addAll(convert(dataEntityMapper.articleEntityMapper.transform(response.body())))
                        if (data?.size!! > LIMIT) {
                            val surplus = data.size - LIMIT
                            val listItemRemove = ArrayList<BaseItemKey>()
                            for (i in 0 until surplus) {
                                data[i].let {
                                    listItemRemove.add(it)
                                }
                            }
                            data.removeAll(listItemRemove)
                        }
                        result.postValue(data)
                    }
                } else {
                    networkState.postValue(
                        Resource.errorPaging(
                            ErrorResource(
                                JSONObject(response.errorBody()?.string()).getString("message"),
                                response.code()
                            ),
                            null,
                            retry = {
                                loadAfter(after, convert)
                            })
                    )
                }
            }
        })
    }

    fun loadBefore(before: Int, convert: (List<ArticleEntity>) -> List<BaseItemKey>) {
        networkState.postValue(
            Resource.loadingPaging(
                null
            )
        )
        iApi.getArticle(before = before).enqueue(object : Callback<List<Article>> {
            override fun onFailure(call: Call<List<Article>>, t: Throwable) {
                networkState.postValue(
                    Resource.errorPaging(
                        ErrorResource(massage = t.message ?: "unknown err"),
                        null,
                        retry = {
                            loadBefore(before, convert)
                        })
                )
            }

            override fun onResponse(call: Call<List<Article>>, response: Response<List<Article>>) {
                if (response.isSuccessful) {
                    val resultRespo = response.body()
                    if (resultRespo?.isEmpty()!!) {
                        networkState.postValue(Resource.successPaging(2))
                    } else {
                        networkState.postValue(Resource.successPaging(null))
                        val data = result.value
                        data?.addAll(convert(dataEntityMapper.articleEntityMapper.transform(response.body())))
                        data?.sortBy {
                            it.idKey
                        }
                        if (data?.size!! > LIMIT) {
                            val surplus = data.size - LIMIT
                            val size = data.size - 1
                            val listItemRemove = ArrayList<BaseItemKey>()
                            for (i in size downTo size - surplus + 1) {
                                data[i].let {
                                    listItemRemove.add(it)
                                }
                            }
                            data.removeAll(listItemRemove)
                        }
                        result.postValue(data)
                    }
                } else {
                    networkState.postValue(
                        Resource.errorPaging(
                            ErrorResource(
                                JSONObject(response.errorBody()?.string()).getString("message"),
                                response.code()
                            ),
                            null,
                            retry = {
                                loadBefore(before, convert)
                            })
                    )
                }
            }
        })
    }
}
package nam.tran.domain.interactor

import androidx.lifecycle.MutableLiveData
import nam.tran.domain.entity.ArticleEntity
import nam.tran.domain.entity.core.BaseItemKey
import nam.tran.domain.entity.core.ItemKeyArticle
import nam.tran.domain.entity.state.ErrorResource
import nam.tran.domain.entity.state.Resource
import nam.tran.domain.executor.AppExecutors
import nam.tran.domain.mapper.DataEntityMapper
import nam.tran.flatform.IApi
import nam.tran.flatform.model.response.Article
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tran.nam.util.Constant.Companion.LIMIT

class ArticleBoundNetwork constructor(
    private val appExecutors: AppExecutors,
    private val dataEntityMapper: DataEntityMapper,
    private val iApi: IApi
) {

    val result = MutableLiveData<ArrayList<ItemKeyArticle<String>>>()
    val networkState = MutableLiveData<Resource<*>>()
    private val listDay = mutableListOf<String>()

    fun initial(convert: (List<ArticleEntity>) -> List<ItemKeyArticle<String>>) {
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
                    appExecutors.networkIO().execute {

                        val data = dataEntityMapper.articleEntityMapper.transform(response.body())

                        val listItem = mutableListOf<ArticleEntity>()
                        for (item in data) {
                            val date = item.headerValue
                            if (!listDay.contains(date)) {
                                listItem.add(ArticleEntity.header(item.id, date))
                                listDay.add(date)
                                listItem.add(item)
                            } else {
                                listItem.add(item)
                            }
                        }
                        networkState.postValue(Resource.success(null))
                        result.postValue(ArrayList(convert(listItem)))
                    }
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

    fun loadAfter(after: Int, convert: (ArticleEntity) -> ItemKeyArticle<String>) {
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
                    appExecutors.networkIO().execute {
                        val resultRespo = response.body()
                        if (resultRespo?.isEmpty()!!) {
                            networkState.postValue(Resource.successPaging(1))
                        } else {
                            val data = result.value!!
                            val listData = dataEntityMapper.articleEntityMapper.transform(response.body())
                            listData.forEachIndexed { index, item ->
                                val date = item.headerValue
                                if (!listDay.contains(date)) {
                                    data.add(
                                        convert(ArticleEntity.header(item.id, date))
                                    )

                                    listDay.add(date)
                                }
                                data.add(convert(item))
                            }
                            if (data.size > LIMIT) {

                                val surplus = data.size - LIMIT
                                val listItemRemove = ArrayList<BaseItemKey>()
                                for (i in 0 until surplus) {
                                    data[i].let {
                                        if (it.isHeader) {
                                            listDay.remove(it.headerValue)
                                        }
                                        listItemRemove.add(it)
                                    }
                                }
                                data.removeAll(listItemRemove)

                                val item = data.get(0)
                                if (!item.isHeader) {
                                    data.add(0, convert(ArticleEntity.header(item.idKey, item.headerValue)))
                                }
                            }
                            networkState.postValue(Resource.successPaging(null))
                            result.postValue(data)
                        }
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

    fun loadBefore(before: Int, convert: (ArticleEntity) -> ItemKeyArticle<String>) {
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
                        val data = result.value!!
                        var firstItem : ItemKeyArticle<String>? = data[0]
                        data.removeAt(0)
                        val listData = dataEntityMapper.articleEntityMapper.transform(response.body())
                        var indexHeader = 0
                        listData.forEachIndexed { index, item ->
                            val date = item.headerValue
                            if (!listDay.contains(date)) {
                                data.add(
                                    index + indexHeader,
                                    convert(ArticleEntity.header(item.id, date))
                                )
                                indexHeader += 1
                                listDay.add(date)
                            }else{
                                if (firstItem != null && firstItem?.isHeader!! && firstItem?.headerValue == item.headerValue){
                                    indexHeader += 1
                                    data.add(index + indexHeader,convert(ArticleEntity.header(firstItem!!.idKey, firstItem!!.headerValue)))
                                    firstItem = null
                                }
                            }
                            data.add(index + indexHeader, convert(item))
                        }

                        if (data.size > LIMIT) {
                            val surplus = data.size - LIMIT
                            val size = data.size - 1
                            val listItemRemove = ArrayList<BaseItemKey>()
                            for (i in size downTo size - surplus + 1) {
                                data[i].let {
                                    if (it.isHeader) {
                                        listDay.remove(it.headerValue)
                                    }
                                    listItemRemove.add(it)
                                }
                            }
                            data.removeAll(listItemRemove)
                        }
                        networkState.postValue(Resource.successPaging(null))
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
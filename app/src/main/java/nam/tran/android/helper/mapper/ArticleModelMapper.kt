/**
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nam.tran.android.helper.mapper

import nam.tran.android.helper.model.ArticleModel
import nam.tran.android.helper.model.ComicModel
import nam.tran.domain.entity.ArticleEntity
import nam.tran.domain.entity.ComicEntity
import nam.tran.domain.entity.state.Resource
import java.util.*
import javax.inject.Inject

/**
 * Mapper class used to transform [ArticleEntity] (in the  in domain layer)
 * to [ArticleModel] in the
 *  app layer.
 */
class ArticleModelMapper @Inject constructor(
    val authorArticleModelMapper: AuthorArticleModelMapper,
    val detailArticleModelMapper: DetailArticleModelMapper
) {

    /**
     * Transform a [ArticleEntity] into an [ArticleModel].
     *
     * @param data Object to be transformed.
     * @return [ArticleModel].
     */
    fun transform(data: ArticleEntity?): ArticleModel {
        data?.let {
            return ArticleModel(
                data.id,
                data.title,
                data.image,
                data.description,
                data.time_ago,
                it.author?.let { authorArticleModelMapper.transform(it) },
                it.detailArticle?.let { detailArticleModelMapper.transform(it) }
            )
        }
    }

    /**
     * Transform a Collection of [ArticleEntity] into a Collection of [ArticleModel].
     *
     * @param datas Objects to be transformed.
     * @return List of [ArticleModel].
     */
    fun transform(datas: List<ArticleEntity>?): List<ArticleModel> {
        val dataModels: MutableList<ArticleModel>

        if (datas != null && !datas.isEmpty()) {
            dataModels = ArrayList()
            for (data in datas) {
                dataModels.add(transform(data))
            }
        } else {
            dataModels = ArrayList()
        }

        return dataModels
    }

    fun transform(data: Resource<List<ArticleEntity>>): Resource<List<ArticleModel>> {
        return Resource(data.status, transform(data.data), data.errorResource, data.loading, data.retry)
    }
}

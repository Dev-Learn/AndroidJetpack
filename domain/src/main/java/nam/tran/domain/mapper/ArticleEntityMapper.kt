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
package nam.tran.domain.mapper

import nam.tran.domain.entity.ArticleEntity
import nam.tran.flatform.model.response.Article
import java.util.*
import javax.inject.Inject

/**
 * Mapper class used to transform [Article] (in the  in flatform layer)
 * to [ArticleEntity] in the
 *  domain layer .
 */
class ArticleEntityMapper @Inject constructor(
    val detailArticleEntityMapper: DetailArticleEntityMapper,
    val authorArticleEntityMapper: AuthorArticleEntityMapper
) {

    /**
     * Transform a [Article] into an [ArticleEntity].
     *
     * @param data Object to be transformed.
     * @return [ArticleEntity].
     */
    fun transform(data: Article?): ArticleEntity {
        data?.let {
            return ArticleEntity(
                it.id,
                it.title,
                it.image,
                it.description,
                it.time_ago,
                it.author?.let { authorArticleEntityMapper.transform(it) },
                it.detailArticle?.let { detailArticleEntityMapper.transform(it) }
            )
        }
    }

    /**
     * Transform a Collection of [Article] into a Collection of [ArticleEntity].
     *
     * @param datas Objects to be transformed.
     * @return List of [ArticleEntity].
     */
    fun transform(datas: List<Article>?): List<ArticleEntity> {
        val dataEntitys: MutableList<ArticleEntity>

        if (datas != null && !datas.isEmpty()) {
            dataEntitys = ArrayList()
            for (data in datas) {
                dataEntitys.add(transform(data))
            }
        } else {
            dataEntitys = ArrayList()
        }

        return dataEntitys
    }
}

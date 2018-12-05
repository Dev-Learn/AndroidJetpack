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

import nam.tran.domain.entity.AuthorArticleEntity
import nam.tran.flatform.model.response.AuthorArticle
import java.util.*
import javax.inject.Inject

/**
 * Mapper class used to transform [AuthorArticle] (in the  in flatform layer)
 * to [AuthorArticleEntity] in the
 *  domain layer .
 */
class AuthorArticleEntityMapper @Inject constructor() {

    /**
     * Transform a [AuthorArticle] into an [AuthorArticleEntity].
     *
     * @param data Object to be transformed.
     * @return [AuthorArticleEntity].
     */
    fun transform(data: AuthorArticle?): AuthorArticleEntity {
        data?.let {
            return AuthorArticleEntity(it.id, it.author_name, it.avarta)
        }
    }

    /**
     * Transform a Collection of [AuthorArticle] into a Collection of [AuthorArticleEntity].
     *
     * @param datas Objects to be transformed.
     * @return List of [AuthorArticleEntity].
     */
    fun transform(datas: List<AuthorArticle>?): List<AuthorArticleEntity> {
        val dataEntitys: MutableList<AuthorArticleEntity>

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

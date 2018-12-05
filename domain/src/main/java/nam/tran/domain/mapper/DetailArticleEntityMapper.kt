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

import nam.tran.domain.entity.DetailArticleEntity
import nam.tran.flatform.model.response.DetailArticle
import java.util.*
import javax.inject.Inject

/**
 * Mapper class used to transform [DetailArticle] (in the  in flatform layer)
 * to [DetailArticleEntity] in the
 *  domain layer .
 */
class DetailArticleEntityMapper @Inject constructor() {

    /**
     * Transform a [DetailArticle] into an [DetailArticleEntity].
     *
     * @param data Object to be transformed.
     * @return [DetailArticleEntity].
     */
    fun transform(data: DetailArticle?): DetailArticleEntity {
        data?.let {
            return DetailArticleEntity(it.id,it.detail)
        }
    }

    /**
     * Transform a Collection of [DetailArticle] into a Collection of [DetailArticleEntity].
     *
     * @param datas Objects to be transformed.
     * @return List of [DetailArticleEntity].
     */
    fun transform(datas: List<DetailArticle>?): List<DetailArticleEntity> {
        val dataEntitys: MutableList<DetailArticleEntity>

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

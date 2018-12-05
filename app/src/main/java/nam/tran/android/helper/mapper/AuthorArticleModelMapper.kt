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

import nam.tran.android.helper.model.AuthorArticleModel
import nam.tran.domain.entity.AuthorArticleEntity
import java.util.*
import javax.inject.Inject

/**
 * Mapper class used to transform [AuthorArticleEntity] (in the  in domain layer)
 * to [AuthorArticleModel] in the
 *  app layer.
 */
class AuthorArticleModelMapper @Inject constructor() {

    /**
     * Transform a [AuthorArticleEntity] into an [AuthorArticleModel].
     *
     * @param data Object to be transformed.
     * @return [AuthorArticleModel].
     */
    fun transform(data: AuthorArticleEntity?): AuthorArticleModel {
        data?.let {
            return AuthorArticleModel(it.id, it.author_name, it.avarta)
        }
    }

    /**
     * Transform a Collection of [AuthorArticleEntity] into a Collection of [AuthorArticleModel].
     *
     * @param datas Objects to be transformed.
     * @return List of [AuthorArticleModel].
     */
    fun transform(datas: List<AuthorArticleEntity>?): List<AuthorArticleModel> {
        val dataModels: MutableList<AuthorArticleModel>

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
}

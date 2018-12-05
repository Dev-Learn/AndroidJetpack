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

import nam.tran.android.helper.model.DetailArticleModel
import nam.tran.domain.entity.DetailArticleEntity
import java.util.*
import javax.inject.Inject

/**
 * Mapper class used to transform [DetailArticleEntity] (in the  in domain layer)
 * to [DetailArticleModel] in the
 *  app layer.
 */
class DetailArticleModelMapper @Inject constructor() {

    /**
     * Transform a [DetailArticleEntity] into an [DetailArticleModel].
     *
     * @param data Object to be transformed.
     * @return [DetailArticleModel].
     */
    fun transform(data: DetailArticleEntity?): DetailArticleModel {
        data?.let {
            return DetailArticleModel(it.id,it.detail)
        }
    }

    /**
     * Transform a Collection of [DetailArticleEntity] into a Collection of [DetailArticleModel].
     *
     * @param datas Objects to be transformed.
     * @return List of [DetailArticleModel].
     */
    fun transform(datas: List<DetailArticleEntity>?): List<DetailArticleModel> {
        val dataModels: MutableList<DetailArticleModel>

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

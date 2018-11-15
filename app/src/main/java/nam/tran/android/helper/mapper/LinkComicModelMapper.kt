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

import nam.tran.android.helper.model.LinkComicModel
import nam.tran.domain.entity.LinkComicEntity
import java.util.*
import javax.inject.Inject

/**
 * Mapper class used to transform [LinkComicEntity] (in the  in domain layer)
 * to [LinkComicModel] in the
 *  app layer.
 */
class LinkComicModelMapper @Inject constructor() {

    /**
     * Transform a [LinkComicEntity] into an [LinkComicModel].
     *
     * @param data Object to be transformed.
     * @return [LinkComicModel].
     */
    fun transform(data: LinkComicEntity?): LinkComicModel {
        if (data == null) {
            throw IllegalArgumentException("Cannot transform a null value")
        }

        return LinkComicModel(data.id,data.idcomic,data.image)
    }

    /**
     * Transform a Collection of [LinkComicEntity] into a Collection of [LinkComicModel].
     *
     * @param datas Objects to be transformed.
     * @return List of [LinkComicModel].
     */
    fun transform(datas: List<LinkComicEntity>?): List<LinkComicModel> {
        val dataModels: MutableList<LinkComicModel>

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

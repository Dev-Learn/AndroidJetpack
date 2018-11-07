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

import nam.tran.android.helper.model.GenreModel
import nam.tran.domain.entity.GenreEntity

import java.util.*
import javax.inject.Inject

/**
 * Mapper class used to transform [GenreEntity] (in the  in domain layer)
 * to [GenreModel] in the
 *  app layer.
 */
class GenreModelMapper @Inject constructor() {

    /**
     * Transform a [GenreEntity] into an [GenreModel].
     *
     * @param data Object to be transformed.
     * @return [GenreModel].
     */
    fun transform(data: GenreEntity?): GenreModel {
        if (data == null) {
            throw IllegalArgumentException("Cannot transform a null value")
        }

        return GenreModel(data.genre)
    }

    /**
     * Transform a Collection of [GenreEntity] into a Collection of [GenreModel].
     *
     * @param datas Objects to be transformed.
     * @return List of [GenreModel].
     */
    fun transform(datas: List<GenreEntity>?): List<GenreModel> {
        val dataModels: MutableList<GenreModel>

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

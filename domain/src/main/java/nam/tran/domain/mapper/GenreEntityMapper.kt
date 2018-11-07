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

import nam.tran.domain.entity.GenreEntity
import nam.tran.flatform.model.response.Genre
import java.util.*
import javax.inject.Inject

/**
 * Mapper class used to transform [Genre] (in the  in flatform layer)
 * to [GenreEntity] in the
 *  domain layer .
 */
class GenreEntityMapper @Inject constructor() {

    /**
     * Transform a [Genre] into an [GenreEntity].
     *
     * @param data Object to be transformed.
     * @return [GenreEntity].
     */
    fun transform(data: Genre?): GenreEntity {
        if (data == null) {
            throw IllegalArgumentException("Cannot transform a null value")
        }

        return GenreEntity(data.genre)
    }

    /**
     * Transform a Collection of [Genre] into a Collection of [GenreEntity].
     *
     * @param datas Objects to be transformed.
     * @return List of [GenreEntity].
     */
    fun transform(datas: List<Genre>?): List<GenreEntity> {
        val dataEntitys: MutableList<GenreEntity>

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

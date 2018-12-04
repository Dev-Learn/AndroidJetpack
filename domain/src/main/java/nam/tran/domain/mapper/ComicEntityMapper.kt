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

import nam.tran.domain.entity.ComicEntity
import nam.tran.flatform.model.response.Comic
import javax.inject.Inject

/**
 * Mapper class used to transform [Comic] (in the  in flatform layer)
 * to [ComicEntity] in the
 *  domain layer .
 */
class ComicEntityMapper @Inject constructor(private val genreEntityMapper: GenreEntityMapper) {

    /**
     * Transform a [Comic] into an [ComicEntity].
     *
     * @param data Object to be transformed.
     * @return [ComicEntity].
     */
    fun transform(data: Comic?): ComicEntity {
        data?.let {
            return ComicEntity(
                data.id,
                data.title,
                data.description,
                data.image,
                ArrayList(genreEntityMapper.transformEntity(data.genre))
            )
        }
    }

    /**
     * Transform a Collection of [Comic] into a Collection of [ComicEntity].
     *
     * @param datas Objects to be transformed.
     * @return List of [ComicEntity].
     */
    fun transformEntity(datas: List<Comic>?): List<ComicEntity> {
        val dataEntitys: MutableList<ComicEntity>

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

    /**
     * Transform a [Comic] into an [Comic].
     *
     * @param data Object to be transformed.
     * @return [Comic].
     */
    fun transform(data: ComicEntity?): Comic {
        if (data == null) {
            throw IllegalArgumentException("Cannot transform a null value")
        }

        return Comic(
            data.id,
            data.title,
            data.description,
            data.image,
            ArrayList(genreEntityMapper.transform(data.genre))
        )
    }

    /**
     * Transform a Collection of [ComicEntity] into a Collection of [Comic].
     *
     * @param datas Objects to be transformed.
     * @return List of [Comic].
     */
    fun transform(datas: List<ComicEntity>?): List<Comic> {
        val data: MutableList<Comic>

        if (datas != null && !datas.isEmpty()) {
            data = ArrayList()
            for (dataItem in datas) {
                data.add(transform(dataItem))
            }
        } else {
            data = ArrayList()
        }

        return data
    }
}

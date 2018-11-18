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


import nam.tran.android.helper.model.ComicModel
import nam.tran.domain.entity.ComicEntity
import nam.tran.domain.entity.state.Resource
import javax.inject.Inject

/**
 * Mapper class used to transform [ComicEntity] (in the  in domain layer)
 * to [ComicModel] in the
 *  app layer.
 */
class ComicModelMapper @Inject constructor(val genreModelMapper: GenreModelMapper) {

    /**
     * Transform a [ComicEntity] into an [ComicModel].
     *
     * @param data Object to be transformed.
     * @return [ComicModel].
     */
    private fun transform(data: ComicEntity?): ComicModel {
        if (data == null) {
            throw IllegalArgumentException("Cannot transform a null value")
        }

        val result = ComicModel(
            data.id,
            data.title,
            data.description,
            data.image,
            ArrayList(genreModelMapper.transform(data.genre))
        )
        result.isLike = data.isLike
        return result
    }

    /**
     * Transform a [ComicModel] into an [ComicEntity].
     *
     * @param data Object to be transformed.
     * @return [ComicEntity].
     */
    fun transformEntity(data: ComicModel?): ComicEntity {
        if (data == null) {
            throw IllegalArgumentException("Cannot transform a null value")
        }

        val result = ComicEntity(
            data.id,
            data.title,
            data.description,
            data.image,
            ArrayList(genreModelMapper.transformEntity(data.genre))
        )
        result.isLike = data.isLike
        return result
    }

    /**
     * Transform a Collection of [ComicEntity] into a Collection of [ComicModel].
     *
     * @param datas Objects to be transformed.
     * @return List of [ComicModel].
     */
    fun transform(datas: List<ComicEntity>?): List<ComicModel> {
        val dataModels: MutableList<ComicModel>

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

    fun transform(data: Resource<List<ComicEntity>>): Resource<List<ComicModel>> {
        return Resource(data.status, transform(data.data), data.message, data.loading, data.retry)
    }
}

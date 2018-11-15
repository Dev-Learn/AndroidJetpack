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

import nam.tran.domain.entity.LinkComicEntity
import nam.tran.flatform.model.response.LinkComic
import java.util.*
import javax.inject.Inject

/**
 * Mapper class used to transform [LinkComic] (in the  in flatform layer)
 * to [LinkComicEntity] in the
 *  domain layer .
 */
class LinkComicEntityMapper @Inject constructor() {

    /**
     * Transform a [LinkComic] into an [LinkComicEntity].
     *
     * @param data Object to be transformed.
     * @return [LinkComicEntity].
     */
    fun transform(data: LinkComic?): LinkComicEntity {
        if (data == null) {
            throw IllegalArgumentException("Cannot transform a null value")
        }

        return LinkComicEntity(data.id,data.idcomic,data.image)
    }

    /**
     * Transform a Collection of [LinkComic] into a Collection of [LinkComicEntity].
     *
     * @param datas Objects to be transformed.
     * @return List of [LinkComicEntity].
     */
    fun transform(datas: List<LinkComic>?): List<LinkComicEntity> {
        val dataEntitys: MutableList<LinkComicEntity>

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

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

import nam.tran.domain.entity.UserEntity
import nam.tran.flatform.model.response.User
import java.util.*
import javax.inject.Inject

/**
 * Mapper class used to transform [User] (in the  in flatform layer)
 * to [UserEntity] in the
 *  domain layer .
 */
class UserEntityMapper @Inject constructor() {

    /**
     * Transform a [User] into an [UserEntity].
     *
     * @param data Object to be transformed.
     * @return [UserEntity].
     */
    fun transform(data: User?): UserEntity {
        if (data == null) {
            throw IllegalArgumentException("Cannot transform a null value")
        }

        return UserEntity(data.id, data.name, data.email, data.avarta)
    }

    /**
     * Transform a Collection of [User] into a Collection of [UserEntity].
     *
     * @param datas Objects to be transformed.
     * @return List of [UserEntity].
     */
    fun transform(datas: List<User>?): List<UserEntity> {
        val dataEntitys: MutableList<UserEntity>

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

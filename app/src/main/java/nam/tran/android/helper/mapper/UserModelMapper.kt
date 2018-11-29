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

import nam.tran.android.helper.model.UserModel
import nam.tran.domain.entity.UserEntity
import java.util.*
import javax.inject.Inject

/**
 * Mapper class used to transform [UserEntity] (in the  in flatform layer)
 * to [UserModel] in the
 *  domain layer .
 */
class UserModelMapper @Inject constructor() {

    /**
     * Transform a [UserEntity] into an [UserModel].
     *
     * @param data Object to be transformed.
     * @return [UserModel].
     */
    fun transform(data: UserEntity?): UserModel {
        if (data == null) {
            throw IllegalArgumentException("Cannot transform a null value")
        }

        return UserModel(data.id,data.name,data.email,data.avarta)
    }

    /**
     * Transform a Collection of [UserEntity] into a Collection of [UserModel].
     *
     * @param datas Objects to be transformed.
     * @return List of [UserModel].
     */
    fun transform(datas: List<UserEntity>?): List<UserModel> {
        val dataEntitys: MutableList<UserModel>

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

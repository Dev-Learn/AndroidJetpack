package nam.tran.android.helper.mapper

import nam.tran.android.helper.model.PreferenceModel
import nam.tran.domain.entity.PreferenceEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceMapper @Inject
internal constructor(private val preferenceModel: PreferenceModel) {

    /**
     * Transform a [PreferenceEntity] into an [PreferenceModel].
     *
     * @param data Object to be transformed.
     * @return [PreferenceModel].
     */
    fun transform(data: PreferenceEntity?) {
        if (data == null) {
            throw IllegalArgumentException("Cannot transform a null value")
        }
        preferenceModel.token = data.token
    }

}

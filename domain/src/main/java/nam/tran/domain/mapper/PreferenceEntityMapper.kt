package nam.tran.domain.mapper

import javax.inject.Inject
import javax.inject.Singleton

import nam.tran.domain.entity.PreferenceEntity
import nam.tran.flatform.local.IPreference

@Singleton
class PreferenceEntityMapper @Inject internal constructor() {

    /**
     * Transform a [IPreference] into an [PreferenceEntity].
     *
     * @param data Object to be transformed.
     * @return [PreferenceEntity].
     */
    fun transform(data: IPreference?): PreferenceEntity {
        if (data == null) {
            throw IllegalArgumentException("Cannot transform a null value")
        }

        return PreferenceEntity()
    }
}

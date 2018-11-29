package nam.tran.domain

import nam.tran.domain.entity.PreferenceEntity
import nam.tran.domain.interactor.app.IAppUseCase
import nam.tran.domain.mapper.DataEntityMapper
import nam.tran.flatform.local.IPreference
import tran.nam.util.Constant
import javax.inject.Inject

class AppUseCase @Inject internal constructor(
    private val iPreference: IPreference,
    private val dataEntityMapper: DataEntityMapper
) : IAppUseCase {
    override fun logout(): PreferenceEntity {
        iPreference.saveToken(Constant.EMPTY)
        return dataEntityMapper.mPreferenceEntityMapper.transform(iPreference)
    }

    override fun getPreference(): PreferenceEntity = dataEntityMapper.mPreferenceEntityMapper.transform(iPreference)

}
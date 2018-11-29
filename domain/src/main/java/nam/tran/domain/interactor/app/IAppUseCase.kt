package nam.tran.domain.interactor.app

import nam.tran.domain.entity.PreferenceEntity

interface IAppUseCase {
    fun getPreference(): PreferenceEntity

    fun logout(): PreferenceEntity
}
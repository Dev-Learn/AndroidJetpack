package nam.tran.android.helper.view.splash.viewmodel

import android.app.Application
import nam.tran.android.helper.mapper.DataMapper
import nam.tran.android.helper.model.PreferenceModel
import nam.tran.domain.interactor.app.IAppUseCase
import tran.nam.core.viewmodel.BaseActivityViewModel
import javax.inject.Inject

class SplashViewModel @Inject internal constructor(
    application: Application, iAppUseCase: IAppUseCase,
    dataMapper: DataMapper,
    val preferenceModel: PreferenceModel
) : BaseActivityViewModel(application) {

    init {
        dataMapper.preferenceMapper.transform(iAppUseCase.getPreference())
    }

    fun isLogin(): Boolean {
        return !preferenceModel.token.isEmpty()
    }

}

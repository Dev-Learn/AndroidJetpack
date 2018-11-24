package nam.tran.android.helper.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import nam.tran.android.helper.view.comic.viewmodel.ComicViewModel
import nam.tran.android.helper.view.detail.viewmodel.DetailComicViewModel
import nam.tran.android.helper.view.local.viewmodel.ComicLocalViewModel
import nam.tran.android.helper.view.login.viewmodel.LoginViewModel
import nam.tran.android.helper.view.register.viewmodel.RegisterViewModel
import nam.tran.android.helper.view.splash.viewmodel.SplashViewModel
import tran.nam.core.di.ViewModelFactory
import tran.nam.core.di.inject.ViewModelKey

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    internal abstract fun bindSplashViewModel(model: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun bindLoginViewModel(model: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    internal abstract fun bindRegisterViewModel(model: RegisterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ComicViewModel::class)
    internal abstract fun bindHomeViewModel(model: ComicViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailComicViewModel::class)
    internal abstract fun bindDetailComicViewModel(model: DetailComicViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ComicLocalViewModel::class)
    internal abstract fun bindComicLocalViewModel(model: ComicLocalViewModel): ViewModel


}

package nam.tran.android.helper.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import nam.tran.android.helper.view.main.detail.viewmodel.DetailComicViewModel
import nam.tran.android.helper.view.main.home.viewmodel.HomeViewModel
import tran.nam.core.di.ViewModelFactory
import tran.nam.core.di.inject.ViewModelKey

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    internal abstract fun bindHomeViewModel(model: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailComicViewModel::class)
    internal abstract fun bindDetailComicViewModel(model: DetailComicViewModel): ViewModel
}

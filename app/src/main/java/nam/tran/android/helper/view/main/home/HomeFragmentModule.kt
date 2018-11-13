package nam.tran.android.helper.view.main.home

import androidx.fragment.app.Fragment

import dagger.Binds
import dagger.Module
import tran.nam.core.di.inject.PerFragment

/**
 * Provides home fragment dependencies.
 */
@Module
abstract class HomeFragmentModule {

    @Binds
    @PerFragment
    internal abstract fun fragmentInject(fragment: HomeFragment): androidx.fragment.app.Fragment
}

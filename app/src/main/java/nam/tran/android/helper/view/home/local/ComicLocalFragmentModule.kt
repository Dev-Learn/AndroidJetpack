package nam.tran.android.helper.view.home.local

import androidx.fragment.app.Fragment

import dagger.Binds
import dagger.Module
import tran.nam.core.di.inject.PerChildFragment

/**
 * Provides comic local fragment dependencies.
 */
@Module
abstract class ComicLocalFragmentModule {

    @Binds
    @PerChildFragment
    internal abstract fun fragmentInject(fragment: ComicLocalFragment): Fragment
}

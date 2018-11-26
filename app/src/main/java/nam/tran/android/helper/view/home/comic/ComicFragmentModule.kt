package nam.tran.android.helper.view.home.comic

import dagger.Binds
import dagger.Module
import tran.nam.core.di.inject.PerChildFragment

/**
 * Provides home fragment dependencies.
 */
@Module
abstract class ComicFragmentModule {

    @Binds
    @PerChildFragment
    internal abstract fun fragmentInject(fragment: ComicFragment): androidx.fragment.app.Fragment
}

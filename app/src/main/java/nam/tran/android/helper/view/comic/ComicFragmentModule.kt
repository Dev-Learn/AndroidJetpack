package nam.tran.android.helper.view.comic

import dagger.Binds
import dagger.Module
import tran.nam.core.di.inject.PerFragment

/**
 * Provides home fragment dependencies.
 */
@Module
abstract class ComicFragmentModule {

    @Binds
    @PerFragment
    internal abstract fun fragmentInject(fragment: ComicFragment): androidx.fragment.app.Fragment
}

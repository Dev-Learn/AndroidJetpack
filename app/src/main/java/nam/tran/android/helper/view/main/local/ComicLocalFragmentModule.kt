package nam.tran.android.helper.view.main.local

import androidx.fragment.app.Fragment

import dagger.Binds
import dagger.Module
import tran.nam.core.di.inject.PerFragment

/**
 * Provides comic local fragment dependencies.
 */
@Module
abstract class ComicLocalFragmentModule {

    @Binds
    @PerFragment
    internal abstract fun fragmentInject(fragment: ComicLocalFragment): Fragment
}

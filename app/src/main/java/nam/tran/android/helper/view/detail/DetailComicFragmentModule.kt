package nam.tran.android.helper.view.detail

import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import tran.nam.core.di.inject.PerFragment

/**
 * Provides detail comic fragment dependencies.
 */
@Module
abstract class DetailComicFragmentModule {

    @Binds
    @PerFragment
    internal abstract fun fragmentInject(fragment: DetailComicFragment): Fragment
}

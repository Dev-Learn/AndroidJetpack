package nam.tran.android.helper.view.home.user

import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import tran.nam.core.di.inject.PerChildFragment

/**
 * Provides user fragment dependencies.
 */
@Module
abstract class UserFragmentModule {

    @Binds
    @PerChildFragment
    internal abstract fun fragmentInject(fragment: UserFragment): Fragment
}

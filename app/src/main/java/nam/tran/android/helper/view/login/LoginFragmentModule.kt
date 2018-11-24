package nam.tran.android.helper.view.login

import androidx.fragment.app.Fragment

import dagger.Binds
import dagger.Module
import tran.nam.core.di.inject.PerFragment

/**
 * Provides login fragment dependencies.
 */
@Module
abstract class LoginFragmentModule {

    @Binds
    @PerFragment
    internal abstract fun fragmentInject(fragment: LoginFragment): Fragment
}

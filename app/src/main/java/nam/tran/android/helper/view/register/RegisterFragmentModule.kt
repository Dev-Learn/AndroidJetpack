package nam.tran.android.helper.view.register

import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import tran.nam.core.di.inject.PerFragment

/**
 * Provides register fragment dependencies.
 */
@Module
abstract class RegisterFragmentModule {

    @Binds
    @PerFragment
    internal abstract fun fragmentInject(fragment: RegisterFragment): Fragment
}

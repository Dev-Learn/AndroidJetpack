package nam.tran.android.helper.view.forgotPassword

import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import tran.nam.core.di.inject.PerFragment

/**
 * Provides forgot password fragment dependencies.
 */
@Module
abstract class ForgotPasswordFragmentModule {

    @Binds
    @PerFragment
    internal abstract fun fragmentInject(fragment: ForgotPasswordFragment): Fragment
}

package nam.tran.android.helper.view.splash

import androidx.appcompat.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import tran.nam.core.di.inject.PerActivity

/**
 * Provides splash activity dependencies.
 */
@Module
abstract class SplashActivityModule {

    @Binds
    @PerActivity
    internal abstract fun activity(activity: SplashActivity): AppCompatActivity
}

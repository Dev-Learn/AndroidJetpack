package nam.tran.android.helper.view.main

import androidx.appcompat.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import nam.tran.android.helper.view.main.detail.DetailComicFragment
import nam.tran.android.helper.view.main.detail.DetailComicFragmentModule
import nam.tran.android.helper.view.main.home.HomeFragment
import nam.tran.android.helper.view.main.home.HomeFragmentModule
import tran.nam.core.di.inject.PerActivity
import tran.nam.core.di.inject.PerFragment

/**
 * Provides main activity dependencies.
 */
@Module
abstract class MainActivityModule {

    /**
     * Provides the injector for the [HomeFragmentModule], which has access to the dependencies
     * provided by this application instance (singleton scoped objects).
     */
    @PerFragment
    @ContributesAndroidInjector(modules = [HomeFragmentModule::class])
    internal abstract fun injectorHomeFragment(): HomeFragment

    /**
    * Provides the injector for the [DetailComicFragmentModule], which has access to the dependencies
    * provided by this application instance (singleton scoped objects).
    */
    @PerFragment
    @ContributesAndroidInjector(modules = [DetailComicFragmentModule::class])
    internal abstract fun injectorDetailComicFragment(): DetailComicFragment

    @Binds
    @PerActivity
    internal abstract fun activity(activity: MainActivity): AppCompatActivity
}

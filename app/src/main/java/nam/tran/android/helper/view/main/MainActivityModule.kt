package nam.tran.android.helper.view.main

import androidx.appcompat.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import nam.tran.android.helper.view.main.local.ComicLocalFragment
import nam.tran.android.helper.view.main.detail.DetailComicFragment
import nam.tran.android.helper.view.main.detail.DetailComicFragmentModule
import nam.tran.android.helper.view.main.comic.ComicFragment
import nam.tran.android.helper.view.main.comic.ComicFragmentModule
import nam.tran.android.helper.view.main.local.ComicLocalFragmentModule
import tran.nam.core.di.inject.PerActivity
import tran.nam.core.di.inject.PerFragment

/**
 * Provides main activity dependencies.
 */
@Module
abstract class MainActivityModule {

    /**
     * Provides the injector for the [ComicFragmentModule], which has access to the dependencies
     * provided by this application instance (singleton scoped objects).
     */
    @PerFragment
    @ContributesAndroidInjector(modules = [ComicFragmentModule::class])
    internal abstract fun injectorHomeFragment(): ComicFragment

    /**
     * Provides the injector for the [ComicLocalFragmentModule], which has access to the dependencies
     * provided by this application instance (singleton scoped objects).
     */
    @PerFragment
    @ContributesAndroidInjector(modules = [ComicLocalFragmentModule::class])
    internal abstract fun injectorComicLocalFragment(): ComicLocalFragment

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

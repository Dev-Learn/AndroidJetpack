package nam.tran.android.helper.view.home

import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import nam.tran.android.helper.view.home.comic.ComicFragment
import nam.tran.android.helper.view.home.comic.ComicFragmentModule
import nam.tran.android.helper.view.home.user.UserFragment
import nam.tran.android.helper.view.home.user.UserFragmentModule
import tran.nam.core.di.inject.PerChildFragment
import tran.nam.core.di.inject.PerFragment

/**
 * Provides home fragment dependencies.
 */
@Module
abstract class HomeFragmentModule {

    /**
     * Provides the injector for the [ComicFragmentModule], which has access to the
     * dependencies provided by this fragment and activity and application instance
     * (singleton scoped objects).
     */
    @PerChildFragment
    @ContributesAndroidInjector(modules = [ComicFragmentModule::class])
    internal abstract fun injectorComicFragment(): ComicFragment

    /**
     * Provides the injector for the [UserFragmentModule], which has access to the
     * dependencies provided by this fragment and activity and application instance
     * (singleton scoped objects).
     */
    @PerChildFragment
    @ContributesAndroidInjector(modules = [UserFragmentModule::class])
    internal abstract fun injectorUserFragment(): UserFragment

    @Binds
    @PerFragment
    internal abstract fun fragment(fragment: HomeFragment): Fragment
}

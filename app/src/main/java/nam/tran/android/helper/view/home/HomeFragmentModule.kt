package nam.tran.android.helper.view.home

import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import nam.tran.android.helper.view.home.comic.ComicFragment
import nam.tran.android.helper.view.home.comic.ComicFragmentModule
import nam.tran.android.helper.view.home.article.ArticleFragment
import nam.tran.android.helper.view.home.article.ArticleFragmentModule
import nam.tran.android.helper.view.home.user.UserFragment
import nam.tran.android.helper.view.home.user.UserFragmentModule
import nam.tran.android.helper.view.home.local.ComicLocalFragment
import nam.tran.android.helper.view.home.local.ComicLocalFragmentModule
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
     * Provides the injector for the [ComicLocalFragmentModule], which has access to the dependencies
     * provided by this application instance (singleton scoped objects).
     */
    @PerChildFragment
    @ContributesAndroidInjector(modules = [ComicLocalFragmentModule::class])
    internal abstract fun injectorComicLocalFragment(): ComicLocalFragment

    /**
     * Provides the injector for the [UserFragmentModule], which has access to the
     * dependencies provided by this fragment and activity and application instance
     * (singleton scoped objects).
     */
    @PerChildFragment
    @ContributesAndroidInjector(modules = [UserFragmentModule::class])
    internal abstract fun injectorUserFragment(): UserFragment

    /**
     * Provides the injector for the [ArticleFragmentModule], which has access to the
     * dependencies provided by this fragment and activity and application instance
     * (singleton scoped objects).
     */
    @PerChildFragment
    @ContributesAndroidInjector(modules = [ArticleFragmentModule::class])
    internal abstract fun injectorTestFragment(): ArticleFragment

    @Binds
    @PerFragment
    internal abstract fun fragment(fragment: HomeFragment): Fragment
}

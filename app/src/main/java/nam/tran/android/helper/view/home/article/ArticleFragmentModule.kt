package nam.tran.android.helper.view.home.article

import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import tran.nam.core.di.inject.PerChildFragment

/**
 * Provides test fragment dependencies.
 */
@Module
abstract class ArticleFragmentModule {

    @Binds
    @PerChildFragment
    internal abstract fun fragmentInject(fragment: ArticleFragment): Fragment
}

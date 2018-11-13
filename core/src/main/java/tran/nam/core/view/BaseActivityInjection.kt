package tran.nam.core.view

import androidx.fragment.app.Fragment
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

/**
 * Abstract Activity for all Activities to extend.
 *
 *
 * **DEPENDENCY INJECTION**
 * We could extend [dagger.android.DaggerActivity] so we can get the boilerplate
 * dagger code for free. However, we want to avoid inheritance (if possible and it is in this case)
 * so that we have to option to inherit from something else later on if needed.
 */
abstract class BaseActivityInjection : BaseActivity(), HasSupportFragmentInjector {

    var fragmentInjector: DispatchingAndroidInjector<androidx.fragment.app.Fragment>? = null
        @Inject set

    override fun inject() {
        AndroidInjection.inject(this)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return fragmentInjector
    }
}

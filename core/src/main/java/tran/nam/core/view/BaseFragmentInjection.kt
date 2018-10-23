package tran.nam.core.view

import android.app.Activity
import android.content.Context
import android.os.Build
import android.support.v4.app.Fragment

import javax.inject.Inject

import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import tran.nam.core.Navigator

@Suppress("DEPRECATION", "OverridingDeprecatedMember")
abstract class BaseFragmentInjection : BaseFragment(), HasSupportFragmentInjector {

    var childFragmentInjector: DispatchingAndroidInjector<Fragment>? = null
        @Inject set

    override fun onAttach(activity: Activity?) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // Perform injection here before M, L (API 22) and below because onAttach(Context)
            // is not yet available at L.
            AndroidSupportInjection.inject(this)
        }
        super.onAttach(activity)
    }

    override fun onAttach(context: Context?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Perform injection here for M (API 23) due to deprecation of onAttach(Activity).
            AndroidSupportInjection.inject(this)
        }
        super.onAttach(context)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return childFragmentInjector
    }

    protected fun addFragmentFromActivity(fragment: BaseFragment) {
        if (activity() != null && activity() is BaseActivityWithFragment && !activity()!!.isFinishing)
            (activity() as BaseActivityWithFragment).addFragment(fragment)
    }

    protected fun showFragmentFromActivity(position: Int) {
        if (activity() != null && activity() is BaseActivityWithFragment && !activity()!!.isFinishing)
            (activity() as BaseActivityWithFragment).showFragment(position)
    }

    protected fun replaceFragmentFromActivity(fragment: BaseFragment) {
        if (activity() != null && activity() is BaseActivityWithFragment && !activity()!!.isFinishing)
            (activity() as BaseActivityWithFragment).replaceFragment(fragment)
    }

    protected fun popFragmentToRoot() {
        if (activity() != null && activity() is BaseActivityWithFragment && !activity()!!.isFinishing)
            (activity() as BaseActivityWithFragment).popToRoot()
    }

    protected fun addFragmentFromFragment(fragment: BaseFragment) {
        if (parentFragment != null && parentFragment is BaseParentFragment) {
            (parentFragment as BaseParentFragment).addChildFragment(fragment)
        }
    }

    protected fun showFragmentFromFragment(position: Int) {
        if (parentFragment != null && parentFragment is BaseParentFragment) {
            (parentFragment as BaseParentFragment).showChildFragment(position)
        }
    }

    protected fun replaceFragmentFromFragment(fragment: BaseFragment) {
        if (parentFragment != null && parentFragment is BaseParentFragment) {
            (parentFragment as BaseParentFragment).replaceChildFragment(fragment)
        }
    }

    protected open fun popChildFragmentToRoot() {
        if (parentFragment != null && parentFragment is BaseParentFragment) {
            (parentFragment as BaseParentFragment).popChildFragmentToRoot()
        }
    }
}

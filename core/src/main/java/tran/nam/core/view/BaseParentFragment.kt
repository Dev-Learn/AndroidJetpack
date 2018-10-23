package tran.nam.core.view

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import tran.nam.core.Navigator
import javax.inject.Inject

abstract class BaseParentFragment : BaseFragmentInjection(), HasSupportFragmentInjector, IFragmentProvider<BaseFragment>, IParentFragmentListener {

    lateinit var mChildFragmentHelper: FragmentHelper<BaseFragment>

    var fragmentInjector: DispatchingAndroidInjector<Fragment>? = null
        @Inject set

    fun addChildFragment(fragment: BaseFragment) {
        mChildFragmentHelper.pushFragment(fragment)
    }

    fun showChildFragment(position: Int) {
        mChildFragmentHelper.showFragment(position)
    }

    fun replaceChildFragment(fragment: BaseFragment) {
        mChildFragmentHelper.replaceFragment(fragment)
    }

    override fun initChildFragment() {
        mChildFragmentHelper = FragmentHelper(this)
        mChildFragmentHelper.setupFragment()
    }

    override fun fragmentManager(): FragmentManager {
        return childFragmentManager
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return fragmentInjector
    }

    public override fun popChildFragmentToRoot() {
        mChildFragmentHelper.popFragmentToRoot()
    }

    override fun popChildFragment(level: Int): Boolean {
        return mChildFragmentHelper.popFragment(level)
    }
}

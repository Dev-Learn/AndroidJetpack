package tran.nam.core.view


import android.support.v4.app.FragmentManager
import javax.inject.Inject

abstract class BaseActivityWithFragment : BaseActivityInjection(), IFragmentProvider<BaseFragment> {

    var mFragmentHelper: FragmentHelper<BaseFragment>? = null
        @Inject set

    fun addFragment(fragment: BaseFragment) {
        mFragmentHelper?.pushFragment(fragment)
    }

    fun showFragment(position: Int) {
        mFragmentHelper?.showFragment(position)
    }

    fun replaceFragment(fragment: BaseFragment) {
        mFragmentHelper?.replaceFragment(fragment)
    }

    override fun initFragment() {
        mFragmentHelper?.setupFragment()
    }

    override fun fragmentManager(): FragmentManager {
        return supportFragmentManager
    }

    fun popToRoot() {
        mFragmentHelper?.popFragmentToRoot()
    }
}

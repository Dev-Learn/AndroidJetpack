package tran.nam.core.view

import android.support.v4.app.FragmentManager

interface IFragmentProvider<T : BaseFragment> {

    val fragments: Array<T>

    val contentLayoutId: Int

    fun fragmentManager(): FragmentManager

}

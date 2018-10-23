package tran.nam.core.view

import android.support.v4.app.FragmentManager
import java.util.*
import javax.inject.Inject

@Suppress("unused")
class FragmentHelper<T : BaseFragment> @Inject
internal constructor(private val fragmentProvider: IFragmentProvider<T>) {

    private lateinit var mPageList: ArrayList<Stack<T>>
    private var mPageIndex: Int = 0
    private var mLayoutId: Int = 0
    private var mFragmentManager: FragmentManager? = null

    private var mBuildFragments: Array<T>? = null

    private var mOnChangedFragmentListener: OnChangedFragmentListener? = null

    fun setupFragment() {
        this.mLayoutId = fragmentProvider.contentLayoutId
        this.mBuildFragments = fragmentProvider.fragments
        this.mFragmentManager = fragmentProvider.fragmentManager()

        initFragments(mBuildFragments!!)
    }

    fun setOnChangedFragmentListener(mOnChangedFragmentListener: OnChangedFragmentListener) {
        this.mOnChangedFragmentListener = mOnChangedFragmentListener
    }

    private fun initFragments(fragments: Array<T>) {
        this.mPageList = ArrayList(fragments.size)

        for (fragment in fragments) {
            val stack = Stack<T>()
            stack.push(fragment)
            this.mPageList.add(stack)
        }

        val fragment = mPageList[mPageIndex].peek()
        if (fragment.isAdded || fragment.isHidden || fragment.isDetached) {
            this.showFragment(mPageIndex)
        } else {
            if (mOnChangedFragmentListener != null)
                mOnChangedFragmentListener!!.onChangedFragment(fragment)
            val transaction = mFragmentManager!!.beginTransaction()
            transaction.add(mLayoutId, fragment)
            transaction.commitAllowingStateLoss()
        }
        this.showFragment(0)
    }

    internal fun pushFragment(fragment: T) {
        val currentStack = mPageList[mPageIndex]
        if (currentStack.peek().tagName == fragment.tagName)
            return

        val hideFragment = currentStack.peek()
        currentStack.push(fragment)
        if (mOnChangedFragmentListener != null){
            mOnChangedFragmentListener!!.onChangedFragment(fragment)
            mOnChangedFragmentListener?.onHideFragment(hideFragment)
        }

        fragment.setCurrentScreen(true)
        fragment.setPush(true)
        hideFragment.setCurrentScreen(true)
        hideFragment.setPush(true)

        val transaction = mFragmentManager!!.beginTransaction()

        if (hideFragment.isShouldSave)
            transaction.hide(hideFragment)
        else
            transaction.detach(hideFragment)

        transaction.add(mLayoutId, fragment)
        transaction.commitAllowingStateLoss()

    }

    fun popFragment(): Boolean {
        return popFragment(1)
    }

    internal fun popFragmentToRoot() {
        val level = mPageList[mPageIndex].size - 1
        popFragment(level)
    }

    fun popFragment(levelPop: Int): Boolean {
        var level = levelPop
        if (level <= 0) return false
        val parentFragment = mPageList[mPageIndex].peek()
        if (parentFragment is IParentFragmentListener && (parentFragment as IParentFragmentListener).popChildFragment(level)) {
            return true
        }
        if (mPageList[mPageIndex].size <= level) return false
        val transaction = mFragmentManager!!.beginTransaction()

        while (level >= 1) {
            val fragment = mPageList[mPageIndex].pop()
            fragment.setCurrentScreen(true)
            fragment.setPush(false)
            transaction.remove(fragment)
            level--
        }
        val showFragment = mPageList[mPageIndex].peek()

        if (mOnChangedFragmentListener != null)
            mOnChangedFragmentListener!!.onChangedFragment(showFragment)

        showFragment.setCurrentScreen(true)
        showFragment.setPush(false)

        if (showFragment.isHidden)
            transaction.show(showFragment)
        else if (showFragment.isDetached)
            transaction.attach(showFragment)

        transaction.commitAllowingStateLoss()
        return true
    }


    fun showFragment(index: Int) {
        if (index == mPageIndex) return
        val showFragment = mPageList[index].peek()
        val hideFragment = mPageList[mPageIndex].peek()
        val transaction = mFragmentManager!!.beginTransaction()

        if (mPageIndex > index) {
            showFragment.setInLeft(true)
            hideFragment.setOutLeft(false)
        } else {
            showFragment.setInLeft(false)
            hideFragment.setOutLeft(true)
        }
        showFragment.setCurrentScreen(false)
        hideFragment.setCurrentScreen(false)
        mPageIndex = index

        if (showFragment.isAdded) {
            if (showFragment.isDetached)
                transaction.attach(showFragment)
            else if (showFragment.isHidden)
                transaction.show(showFragment)
        } else {
            transaction.add(mLayoutId, showFragment)
        }
        if (hideFragment.isShouldSave)
            transaction.hide(hideFragment)
        else
            transaction.detach(hideFragment)
        transaction.commitAllowingStateLoss()
        if (mOnChangedFragmentListener != null){
            mOnChangedFragmentListener?.onHideFragment(hideFragment)
            mOnChangedFragmentListener!!.onChangedFragment(showFragment)
        }
    }

    internal fun replaceFragment(fragment: T) {
        val transaction = mFragmentManager!!.beginTransaction()
        transaction.replace(mLayoutId, fragment)
        mPageList[mPageIndex].pop()
        mPageList[mPageIndex].push(fragment)
        fragment.setCurrentScreen(true)
        fragment.setPush(true)
        transaction.commitAllowingStateLoss()
    }

    fun release() {
        mOnChangedFragmentListener = null
    }

    fun getCurrentFragment(): BaseFragment? {
        return mPageList[mPageIndex].peek()
    }

    interface OnChangedFragmentListener {
        fun onChangedFragment(fragment: BaseFragment)
        fun onHideFragment(fragment: BaseFragment)
    }
}

/*
 * Copyright 2017 Vandolf Estrellado
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tran.nam.core.view

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import tran.nam.core.R

abstract class BaseFragment : androidx.fragment.app.Fragment() {

    protected var mIsCurrentScreen: Boolean = false
    private var mIsInLeft: Boolean = false
    private var mIsOutLeft: Boolean = false
    private var mIsPush: Boolean = false
    var isInitialized = true
    var isViewCreated = false
    private var mViewDestroyed = false
    private var mWaitThread: WaitThread? = null

    /**
     * @return layout resource id
     */
    @LayoutRes
    protected abstract fun layoutId(): Int

    protected open fun isHaveAnimation(): Boolean{
        return true
    }

    val tagName: String = javaClass.simpleName

    val isShouldSave: Boolean = true

    protected open fun pushExitAnimId(): Int{
        return R.anim.slide_left_out
    }

    protected open fun popEnterAnimId(): Int{
        return R.anim.slide_left_in
    }

    protected open fun popExitAnimId(): Int{
        return R.anim.slide_right_out
    }

    protected open fun pushEnterAnimId(): Int{
        return R.anim.slide_right_in
    }

    protected open fun leftInAnimId(): Int{
        return  R.anim.slide_left_in
    }

    protected open fun rightInAnimId(): Int{
        return R.anim.slide_right_in
    }

    protected open fun leftOutAnimId(): Int{
        return R.anim.slide_left_out
    }

    protected open fun rightOutAnimId(): Int{
        return R.anim.slide_right_out
    }

    /*
     * Init Child Fragment Helper
     **/
    protected open fun initChildFragment() {}

    open fun initLayout(inflater: LayoutInflater, container: ViewGroup?): View {
        return inflater.inflate(layoutId(), container, false)
    }

    fun activity(): BaseActivity? {
        return activity as BaseActivity?
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = initLayout(inflater, container)
        initChildFragment()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onVisible()
        isViewCreated = true
        mViewDestroyed = false
        if (mWaitThread != null)
            mWaitThread!!.continueProcessing()
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation {
        val animation: Animation
        if (isHaveAnimation()) {
            if (mIsCurrentScreen) {
                val popExit = popExitAnimId()
                val pushEnter = pushEnterAnimId()
                val pushExit = pushExitAnimId()
                val popEnter = popEnterAnimId()
                animation = if (mIsPush)
                    AnimationUtils.loadAnimation(activity(), if (enter) pushEnter else pushExit)
                else
                    AnimationUtils.loadAnimation(activity(), if (enter) popEnter else popExit)
            } else {
                if (enter) {
                    val left = leftInAnimId()
                    val right = rightInAnimId()
                    if (mIsInLeft) {
                        if (left == 0) {
                            animation = AlphaAnimation(1f, 1f)
                            animation.setDuration(resources.getInteger(R.integer.animation_time_full).toLong())
                        } else {
                            animation = AnimationUtils.loadAnimation(activity(), left)
                        }
                    } else {
                        if (right == 0) {
                            animation = AlphaAnimation(1f, 1f)
                            animation.setDuration(resources.getInteger(R.integer.animation_time_full).toLong())
                        } else {
                            animation = AnimationUtils.loadAnimation(activity(), right)
                        }
                    }
                } else {
                    val left = leftOutAnimId()
                    val right = rightOutAnimId()
                    animation = AnimationUtils.loadAnimation(activity(), if (mIsOutLeft) left else right)
                }
            }
        } else {
            animation = AnimationUtils.loadAnimation(context, R.anim.no_anim)
        }

        if (enter) {
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}

                override fun onAnimationEnd(animation: Animation) {
                    if (mViewDestroyed)
                        return
                    mWaitThread = WaitThread(this@BaseFragment)
                    mWaitThread!!.start()
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
        }
        return animation
    }

    override fun onDestroyView() {
//     This lifecycle method still gets called even if onCreateView returns a null view.
        mViewDestroyed = true
        isViewCreated = false
        onInVisible()
        if (mWaitThread != null)
            mWaitThread!!.stopProcessing()
        super.onDestroyView()
    }

    protected fun showLoadingDialog() {
        if (activity != null && activity is BaseActivity && !activity!!.isFinishing)
            (activity as BaseActivity).showLoadingDialog()
    }

    protected fun hideLoadingDialog() {
        if (activity != null && activity is BaseActivity && !activity!!.isFinishing)
            (activity as BaseActivity).hideLoadingDialog()
    }

    protected fun showKeyboard() {
        if (activity() != null && !activity()!!.isFinishing) {
            activity()!!.showKeyboard()
        }
    }

    protected fun hideKeyboard() {
        if (activity() != null && !activity()!!.isFinishing)
            activity()!!.hideKeyboard()
    }

    open fun initialized() {
        isInitialized = false
    }

    fun setInLeft(inLeft: Boolean) {
        mIsInLeft = inLeft
    }

    fun setOutLeft(outLeft: Boolean) {
        mIsOutLeft = outLeft
    }

    fun setCurrentScreen(currentScreen: Boolean) {
        mIsCurrentScreen = currentScreen
    }

    fun setPush(push: Boolean) {
        mIsPush = push
    }

    protected open fun onVisible() {}

    fun handleAfterVisible() {}

    protected fun onInVisible() {}
}
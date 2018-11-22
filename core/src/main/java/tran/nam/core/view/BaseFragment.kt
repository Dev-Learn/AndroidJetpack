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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

abstract class BaseFragment : androidx.fragment.app.Fragment() {

    /**
     * @return layout resource id
     */
    @LayoutRes
    protected abstract fun layoutId(): Int

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


    protected open fun onVisible() {}
}
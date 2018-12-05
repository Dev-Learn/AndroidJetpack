/*
 *  Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://mindorks.com/license/apache-v2
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package tran.nam.core.view.mvvm

import androidx.lifecycle.ViewModelProvider
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.widget.EditText
import android.widget.Toast
import tran.nam.core.view.BaseActivityInjection
import tran.nam.core.viewmodel.BaseActivityViewModel
import tran.nam.core.viewmodel.IView
import javax.inject.Inject

abstract class BaseActivityMVVM<V : ViewDataBinding, VM : BaseActivityViewModel> : BaseActivityInjection(), IView {

    var mViewModelFactory: ViewModelProvider.Factory? = null
        @Inject set

    protected var mViewModel: VM? = null

    protected lateinit var mViewDataBinding: V

    override fun initLayout() {
        mViewDataBinding = DataBindingUtil.setContentView(this, layoutId())
    }

    override fun init(savedInstanceState: Bundle?) {
        mViewModel?.onCreated(this)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mViewModel?.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        mViewModel?.onRestoreInstanceState(savedInstanceState)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v != null && v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    hideKeyboard()
                }
            }
        }
        return try {
            super.dispatchTouchEvent(event)
        } catch (e: Exception) {
            true
        }

    }

    override fun onDestroy() {
        this.mViewDataBinding.unbind()
        super.onDestroy()
    }

    override fun showDialogLoading() {
        showLoadingDialog()
    }

    override fun hideDialogLoading() {
        hideLoadingDialog()
    }

    override fun onShowDialogError(message: String?, codeError: Int?) {
        hideLoadingDialog()
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}


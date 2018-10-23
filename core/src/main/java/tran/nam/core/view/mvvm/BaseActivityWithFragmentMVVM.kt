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

import android.arch.lifecycle.ViewModelProvider
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.widget.Toast
import tran.nam.core.view.BaseActivityWithFragment
import tran.nam.core.viewmodel.BaseActivityViewModel
import tran.nam.core.viewmodel.IViewModel
import javax.inject.Inject

abstract class BaseActivityWithFragmentMVVM<V : ViewDataBinding, VM : BaseActivityViewModel> : BaseActivityWithFragment(), IViewModel {

    var mViewModelFactory: ViewModelProvider.Factory? = null
        @Inject set

    protected var mViewModel: VM? = null

    protected lateinit var mViewDataBinding: V

    override fun initLayout() {
        mViewDataBinding = DataBindingUtil.setContentView(this, layoutId())
    }

    override fun initData(savedInstanceState: Bundle?) {
        if (mViewModel != null)
            mViewModel!!.onCreated(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (mViewModel != null)
            mViewModel!!.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (mViewModel != null)
            mViewModel!!.onRestoreInstanceState(savedInstanceState)
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

    override fun onShowDialogError(message: String?) {
        Toast.makeText(this, "Error Dialog", Toast.LENGTH_SHORT).show()
    }
}


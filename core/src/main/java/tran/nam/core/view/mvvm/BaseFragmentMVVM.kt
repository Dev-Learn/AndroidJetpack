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
import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import tran.nam.common.autoCleared
import tran.nam.core.view.BaseFragmentInjection
import tran.nam.core.viewmodel.BaseFragmentViewModel
import tran.nam.core.viewmodel.IViewModel
import javax.inject.Inject

abstract class BaseFragmentMVVM<V : ViewDataBinding, VM : BaseFragmentViewModel> : BaseFragmentInjection(), IViewModel {

    /**
     * MVVM ViewModel ViewModelProvider.Factory
     */
    var mViewModelFactory: ViewModelProvider.Factory? = null
        @Inject set

    protected var mViewModel: VM? = null

    protected lateinit var mViewDataBinding: V

    protected var binding by autoCleared<V>()

    abstract fun initViewModel(factory: ViewModelProvider.Factory?)

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        initViewModel(mViewModelFactory)
        mViewModel?.onAttach(this)
    }

    override fun initLayout(inflater: LayoutInflater, container: ViewGroup?): View {
        mViewDataBinding = DataBindingUtil.inflate(inflater, layoutId(), container, false)
        binding = mViewDataBinding
        return mViewDataBinding.root
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
        hideDialogLoading()
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}

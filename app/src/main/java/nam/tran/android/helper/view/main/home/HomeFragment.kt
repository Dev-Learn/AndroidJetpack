package nam.tran.android.helper.view.main.home;

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import nam.tran.android.helper.R

import nam.tran.android.helper.view.main.home.viewmodel.IHomeViewModel
import nam.tran.android.helper.view.main.home.viewmodel.HomeViewModel

import nam.tran.android.helper.databinding.FragmentHomeBinding
import nam.tran.android.helper.model.ComicModel
import nam.tran.domain.entity.ComicEntity

import tran.nam.core.view.mvvm.BaseFragmentMVVM
import tran.nam.util.Logger

class HomeFragment : BaseFragmentMVVM<FragmentHomeBinding, HomeViewModel>(), IHomeViewModel {

    override fun initViewModel(factory: ViewModelProvider.Factory?) {
        mViewModel = ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)
    }

    override fun layoutId(): Int {
        return R.layout.fragment_home
    }

    override fun onVisible() {
        super.onVisible()
        mViewDataBinding.viewModel = mViewModel
    }

    override fun updateData(data: List<ComicModel>) {
       Logger.debug(data)
    }

    override fun updateView() {
        mViewDataBinding.viewModel = mViewModel
        mViewDataBinding.executePendingBindings()
    }
}

package nam.tran.android.helper.view.main.home;

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import nam.tran.android.helper.R

import nam.tran.android.helper.view.main.home.viewmodel.IHomeViewModel
import nam.tran.android.helper.view.main.home.viewmodel.HomeViewModel

import nam.tran.android.helper.databinding.FragmentHomeBinding
import nam.tran.android.helper.model.ComicModel
import nam.tran.domain.entity.ComicEntity
import tran.nam.common.autoCleared
import tran.nam.core.biding.FragmentDataBindingComponent

import tran.nam.core.view.mvvm.BaseFragmentMVVM
import tran.nam.util.Logger

class HomeFragment : BaseFragmentMVVM<FragmentHomeBinding, HomeViewModel>(), IHomeViewModel {

    private val dataBindingComponent = FragmentDataBindingComponent(this)
    private var adapter by autoCleared<ComicAdapter>()

    override fun initViewModel(factory: ViewModelProvider.Factory?) {
        mViewModel = ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)
    }

    override fun layoutId(): Int {
        return R.layout.fragment_home
    }

    override fun onVisible() {
        mViewDataBinding.viewModel = mViewModel

        adapter = ComicAdapter(dataBindingComponent)

        binding.rvComic.addItemDecoration(DividerItemDecoration(context, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL))
        binding.rvComic.adapter = adapter
    }

    override fun updateData(data: List<ComicModel>) {
        adapter.replace(data)
    }

    override fun updateView() {
        mViewDataBinding.viewModel = mViewModel
        mViewDataBinding.executePendingBindings()
    }
}

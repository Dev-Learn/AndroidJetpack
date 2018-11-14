@file:Suppress("UNCHECKED_CAST")

package nam.tran.android.helper.view.main.home;

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import nam.tran.android.helper.R
import nam.tran.android.helper.databinding.FragmentHomeBinding
import nam.tran.android.helper.model.ComicModel
import nam.tran.android.helper.view.main.home.viewmodel.HomeViewModel
import nam.tran.android.helper.view.main.home.viewmodel.IHomeViewModel
import tran.nam.common.autoCleared
import tran.nam.core.biding.FragmentDataBindingComponent
import tran.nam.core.view.mvvm.BaseFragmentMVVM

class HomeFragment : BaseFragmentMVVM<FragmentHomeBinding, HomeViewModel>(), IHomeViewModel {

    private val dataBindingComponent = FragmentDataBindingComponent(this)
//    private var adapter by autoCleared<ComicAdapter>()

    override fun initViewModel(factory: ViewModelProvider.Factory?) {
        mViewModel = ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)
    }

    override fun layoutId(): Int {
        return R.layout.fragment_home
    }

    override fun onVisible() {
        mViewDataBinding.viewModel = mViewModel

        val adapter = ComicAdapterPaging(dataBindingComponent)

//        adapter = ComicAdapter(dataBindingComponent)

        binding.rvComic.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
        binding.rvComic.adapter = adapter

        mViewModel?.posts?.observe(this, Observer {
            adapter.submitList(it as PagedList<ComicModel>)
        })

        mViewModel?.networkState?.observe(this, Observer {
            adapter.setNetworkState(it)
        })
    }

    override fun updateData(data: List<ComicModel>) {
//        adapter.replace(data)
    }

    override fun updateView() {
//        mViewDataBinding.viewModel = mViewModel
//        mViewDataBinding.executePendingBindings()
    }
}

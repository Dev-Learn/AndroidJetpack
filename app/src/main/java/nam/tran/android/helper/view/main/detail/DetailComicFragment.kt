package nam.tran.android.helper.view.main.detail;

import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import nam.tran.android.helper.R
import nam.tran.android.helper.databinding.FragmentDetailComicBinding
import nam.tran.android.helper.model.ComicModel
import nam.tran.android.helper.model.LinkComicModel
import nam.tran.android.helper.view.main.detail.viewmodel.DetailComicViewModel
import nam.tran.android.helper.view.main.detail.viewmodel.IDetailComicViewModel
import nam.tran.android.helper.view.main.home.ComicAdapterPaging
import tran.nam.core.biding.FragmentDataBindingComponent
import tran.nam.core.view.mvvm.BaseFragmentMVVM

@Suppress("UNCHECKED_CAST")
class DetailComicFragment : BaseFragmentMVVM<FragmentDetailComicBinding, DetailComicViewModel>(),
    IDetailComicViewModel {

    private val dataBindingComponent = FragmentDataBindingComponent(this)

    override fun initViewModel(factory: ViewModelProvider.Factory?) {
        mViewModel = ViewModelProviders.of(this, factory).get(DetailComicViewModel::class.java)
    }

    override fun layoutId(): Int {
        return R.layout.fragment_detail_comic
    }

    override fun onVisible() {
        mViewDataBinding.viewModel = mViewModel

        val adapter = DetailComicAdapterPaging(dataBindingComponent){
            mViewModel?.retry()
        }

        binding.rvLinkComic.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
        binding.rvLinkComic.adapter = adapter

        mViewModel?.posts?.observe(this, Observer {
            adapter.submitList(it as PagedList<LinkComicModel>)
        })

        mViewModel?.networkState?.observe(this, Observer {
            adapter.setNetworkState(it)
        })

        mViewModel?.getData(arguments?.get("comic") as ComicModel)
    }
}

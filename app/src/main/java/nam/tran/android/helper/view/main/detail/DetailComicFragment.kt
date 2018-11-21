package nam.tran.android.helper.view.main.detail;

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
import tran.nam.core.biding.FragmentDataBindingComponent
import tran.nam.core.view.mvvm.BaseFragmentMVVM
import tran.nam.util.Logger

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

        val adapter = DetailComicAdapterPaging(dataBindingComponent)

        binding.rvLinkComic.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
        binding.rvLinkComic.adapter = adapter

        mViewModel?.getData(arguments?.get("comic") as ComicModel,arguments?.getBoolean("isLocal"))

        mViewModel?.posts?.observe(this, Observer {
//            Logger.debug(it)
            adapter.submitList(it as PagedList<LinkComicModel>)
        })

        mViewModel?.results?.observe(this, Observer { it ->
            it?.let {
                if (it.initial) {
                    mViewDataBinding.viewModel = mViewModel
                    mViewDataBinding.executePendingBindings()
                } else {
                    adapter.setNetworkState(it)
                }
            }
        })
    }
}

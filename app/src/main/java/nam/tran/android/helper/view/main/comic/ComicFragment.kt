@file:Suppress("UNCHECKED_CAST")

package nam.tran.android.helper.view.main.comic;

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.paging.PagedList
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import nam.tran.android.helper.R
import nam.tran.android.helper.databinding.FragmentComicBinding
import nam.tran.android.helper.model.ComicModel
import nam.tran.android.helper.view.main.comic.viewmodel.ComicViewModel
import nam.tran.android.helper.view.main.comic.viewmodel.IHomeViewModel
import tran.nam.core.biding.FragmentDataBindingComponent
import tran.nam.core.view.mvvm.BaseFragmentMVVM

class ComicFragment : BaseFragmentMVVM<FragmentComicBinding, ComicViewModel>(), IHomeViewModel {

    private val dataBindingComponent = FragmentDataBindingComponent(this)

    override fun initViewModel(factory: ViewModelProvider.Factory?) {
        mViewModel = ViewModelProviders.of(this, factory).get(ComicViewModel::class.java)
    }

    override fun layoutId(): Int {
        return R.layout.fragment_comic
    }

    override fun onVisible() {
        mViewDataBinding.viewModel = mViewModel

        val adapter = ComicAdapterPaging(dataBindingComponent) {
            mViewModel?.like(it)
        }

//

        binding.fab.setOnClickListener {
            it.findNavController().navigate(R.id.localComicFragment)
        }

        binding.rvComic.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
        binding.rvComic.adapter = adapter

//        mViewModel?.results?.observe(this, Observer {
//            if (it?.data != null && it.data!!.isNotEmpty()) {
//                adapter.replace(it.data!!)
//            }
//        mViewDataBinding.viewModel = mViewModel
//        mViewDataBinding.executePendingBindings()
//        })

        mViewModel?.posts?.observe(this, Observer {
            adapter.submitList(it as PagedList<ComicModel>)
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

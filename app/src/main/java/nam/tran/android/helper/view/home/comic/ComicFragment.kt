@file:Suppress("UNCHECKED_CAST")

package nam.tran.android.helper.view.home.comic;

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.paging.PagedList
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import nam.tran.android.helper.R
import nam.tran.android.helper.databinding.FragmentComicBinding
import nam.tran.android.helper.model.ComicModel
import nam.tran.android.helper.view.home.comic.viewmodel.ComicViewModel
import nam.tran.android.helper.view.home.comic.viewmodel.IComicViewModel
import tran.nam.core.biding.FragmentDataBindingComponent
import tran.nam.core.view.mvvm.BaseFragmentMVVM

class ComicFragment : BaseFragmentMVVM<FragmentComicBinding, ComicViewModel>(),
    IComicViewModel {

    private val dataBindingComponent = FragmentDataBindingComponent(this)

    override fun initViewModel(factory: ViewModelProvider.Factory?) {
        mViewModel = ViewModelProviders.of(this, factory).get(ComicViewModel::class.java)
    }

    override fun layoutId(): Int {
        return R.layout.fragment_comic
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding?.viewModel = mViewModel

        val adapter = ComicAdapterPaging(dataBindingComponent, {
            mViewModel?.like(it)
        },{
            val bundle = bundleOf("comic" to it)
            Navigation.findNavController(requireActivity().findViewById<View>(R.id.nav_host_fragment)).navigate(R.id.action_homeFragment_to_detailComicFragment,bundle)
        })

        binding.fab.setOnClickListener {
            Navigation.findNavController(requireActivity().findViewById<View>(R.id.nav_host_fragment)).navigate(R.id.action_homeFragment_to_localComicFragment)
        }

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

        mViewModel?.results?.observe(this, Observer { result ->
            result?.let {
                if (it.initial) {
                    mViewDataBinding?.viewModel = mViewModel
                } else {
                    adapter.setNetworkState(it)
                }
            }
        })
    }
}

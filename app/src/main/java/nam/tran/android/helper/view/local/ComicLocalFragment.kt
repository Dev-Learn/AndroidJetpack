package nam.tran.android.helper.view.local;


import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import nam.tran.android.helper.R
import nam.tran.android.helper.databinding.FragmentComicLocalBinding
import nam.tran.android.helper.view.local.viewmodel.ComicLocalViewModel
import nam.tran.android.helper.view.local.viewmodel.IComicLocalViewModel
import tran.nam.common.autoCleared
import tran.nam.core.biding.FragmentDataBindingComponent
import tran.nam.core.view.mvvm.BaseFragmentMVVM

class ComicLocalFragment : BaseFragmentMVVM<FragmentComicLocalBinding, ComicLocalViewModel>(),
    IComicLocalViewModel {

    private val dataBindingComponent = FragmentDataBindingComponent(this)

    private var adapter by autoCleared<ComicAdapter>()

    override fun initViewModel(factory: ViewModelProvider.Factory?) {
        mViewModel = ViewModelProviders.of(this, factory).get(ComicLocalViewModel::class.java)
    }

    override fun layoutId(): Int {
        return R.layout.fragment_comic_local
    }

    override fun onVisible() {
        mViewDataBinding.viewModel = mViewModel

        adapter = ComicAdapter(dataBindingComponent)

        binding.rvComicLocal.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
        binding.rvComicLocal.adapter = adapter

        mViewModel?.results?.observe(this, Observer {
            if (it?.data != null && it.data!!.isNotEmpty()) {
                adapter.replace(it.data!!)
            }
            mViewDataBinding.viewModel = mViewModel
            mViewDataBinding.executePendingBindings()
        })
    }
}

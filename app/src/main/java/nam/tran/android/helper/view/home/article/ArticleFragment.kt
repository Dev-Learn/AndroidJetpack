package nam.tran.android.helper.view.home.article

import android.app.Activity
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import nam.tran.android.helper.R
import nam.tran.android.helper.databinding.FragmentArticleBinding
import nam.tran.android.helper.model.ArticleModel
import nam.tran.android.helper.view.home.article.ArticleFragment.TYPE.AFTER
import nam.tran.android.helper.view.home.article.ArticleFragment.TYPE.BEFORE
import nam.tran.android.helper.view.home.article.viewmodel.ArticleViewModel
import nam.tran.android.helper.view.home.article.viewmodel.IArticleView
import tran.nam.common.autoCleared
import tran.nam.core.biding.FragmentDataBindingComponent
import tran.nam.core.view.mvvm.BaseFragmentMVVM
import tran.nam.util.Logger

class ArticleFragment : BaseFragmentMVVM<FragmentArticleBinding, ArticleViewModel>(), IArticleView {

    private val dataBindingComponent = FragmentDataBindingComponent(this)

    private var isLoading: Boolean = false
    private var isLoadBefore: Boolean = false
    private var isLoadAfter: Boolean = true
    private var mNumberToLoadMore = 1
    private var type = AFTER

    override fun initViewModel(factory: ViewModelProvider.Factory?) {
        mViewModel = ViewModelProviders.of(this, factory).get(ArticleViewModel::class.java)
    }

    override fun layoutId(): Int {
        return R.layout.fragment_article
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.debug("AAAAAAAAAAA - onCreate")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(savedInstanceState != null)
            pos = savedInstanceState.getLong("StateRv")
    }

    override fun onAttachFragment(childFragment: Fragment?) {
        super.onAttachFragment(childFragment)
        Logger.debug("AAAAAAAAAAA - onAttachFragment")
    }

    override fun onResume() {
        super.onResume()
        Logger.debug("AAAAAAAAAAA - " + pos)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Logger.debug("onViewCreated - " + pos)
        mViewDataBinding?.viewModel = mViewModel

        val adapter = ArticleAdapter(dataBindingComponent,{
            isLoadAfter = true
        },{
            isLoadBefore = true
        },{
            isLoading = false
        }, {
            if (pos != 0L) {
                Logger.debug("AAAAAAAAAAA - " + pos)
                binding.rvArticle.layoutManager?.scrollToPosition(pos.toInt());
                pos = 0L
            }
        })

        binding.rvArticle.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )

        binding.rvArticle.adapter = adapter

        binding.rvArticle.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager
                layoutManager?.let { it ->
                    val totalItemCount = it.itemCount
                    if (it is LinearLayoutManager) {
                        val lastVisibleItemPosition = it.findLastVisibleItemPosition()
                        val firstVisibleItemPosition = it.findFirstVisibleItemPosition()
                        Logger.debug("loadBefore - firstVisibleItemPosition: $firstVisibleItemPosition")
                        Logger.debug("loadBefore - isLoading: $isLoading")
                        Logger.debug("loadBefore - isOverLimit: " + adapter.isOverLimit())
                        Logger.debug("loadBefore - isLoadBefore: $isLoadBefore")

                        if (!isLoading && totalItemCount <= lastVisibleItemPosition + mNumberToLoadMore && isLoadAfter) {
                            type = AFTER
                            mViewModel?.loadMore((adapter.getItemLasted()))
                            isLoading = true
                        } else if (!isLoading && firstVisibleItemPosition == 0 && adapter.isOverLimit() && isLoadBefore) {
                            Logger.debug("loadBefore - CallApi")
                            type = BEFORE
                            mViewModel?.loadBefore(adapter.getItemFirst())
                            isLoading = true
                        }
                    }
                }
            }
        })

        mViewModel?.data?.observe(viewLifecycleOwner, Observer {
            @Suppress("UNCHECKED_CAST")
            Logger.debug("AAAAAAAAAAA size - " + it.size)

            adapter.add(it as List<ArticleModel>, type == AFTER)
        })

        mViewModel?.results?.observe(viewLifecycleOwner, Observer {
            if (it.initial) {
                mViewDataBinding?.viewModel = mViewModel
            } else {
                if (it.isSuccess()) {
                    if (it.data != null){
                        when (it.data) {
                            1 -> isLoadAfter = false
                            2 -> isLoadBefore = false
                        }
                        isLoading = false
                    }else{
                        isLoadAfter = true
                        isLoadBefore = true
                    }
                }
                adapter.setNetworkState(it, type == AFTER)
            }
        })
    }

    var pos: Long = 0L

    override fun onSaveInstanceState(outState: Bundle) {
        Logger.debug("AAAAAAAAAAA - onSaveInstanceState")
        pos = (binding.rvArticle.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition().toLong()
        outState.putLong("StateRv", pos);
    }




    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Logger.debug("AAAAAAAAAAA - onViewStateRestored")
    }

    enum class TYPE {
        AFTER, BEFORE
    }
}

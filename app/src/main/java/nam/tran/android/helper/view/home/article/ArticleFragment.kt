package nam.tran.android.helper.view.home.article;

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import nam.tran.android.helper.R
import nam.tran.android.helper.databinding.FragmentArticleBinding
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

    private var adapter by autoCleared<ArticleAdapter>()

    private var isLoading: Boolean = false
    private var isLoadBefore: Boolean = false
    private var mNumberToLoadMore = 1
    private var type = AFTER

    override fun initViewModel(factory: ViewModelProvider.Factory?) {
        mViewModel = ViewModelProviders.of(this, factory).get(ArticleViewModel::class.java)
    }

    override fun layoutId(): Int {
        return R.layout.fragment_article
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mViewDataBinding?.viewModel = mViewModel

        adapter = ArticleAdapter(dataBindingComponent) {
            isLoadBefore = true
        }

        binding.rvArticle.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
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

                        if (!isLoading && totalItemCount <= lastVisibleItemPosition + mNumberToLoadMore) {
                            type = AFTER
                            mViewModel?.loadMore((adapter.getItemLasted()))
                            isLoading = true
                        } else if (!isLoading && firstVisibleItemPosition == 0 && adapter.isOverLimit() && isLoadBefore) {
                            type = BEFORE
                            mViewModel?.loadBefore(adapter.getItemFirst())
                            isLoading = true
                        }
                    }
                }
            }
        })
        binding.rvArticle.adapter = adapter

        mViewModel?.results?.observe(viewLifecycleOwner, Observer {
            if (it.initial) {
                if (it?.data != null && it.data!!.isNotEmpty()) {
                    adapter.replace(it.data!!)
                }
                mViewDataBinding?.viewModel = mViewModel
                mViewDataBinding?.executePendingBindings()
            } else {
                adapter.setNetworkState(it, type == AFTER)
                if (it?.data != null && it.data!!.isNotEmpty()) {
                    isLoading = false
                    adapter.add(it.data!!, type == AFTER)
                } else if (it.isSuccess() && type == BEFORE) {
                    isLoading = false
                    isLoadBefore = false
                }
            }
        })
    }

    enum class TYPE {
        AFTER, BEFORE
    }
}

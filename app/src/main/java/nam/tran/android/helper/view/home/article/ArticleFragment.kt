package nam.tran.android.helper.view.home.article

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
import nam.tran.android.helper.model.ArticleModel
import nam.tran.android.helper.view.home.article.ArticleFragment.TYPE.AFTER
import nam.tran.android.helper.view.home.article.ArticleFragment.TYPE.BEFORE
import nam.tran.android.helper.view.home.article.viewmodel.ArticleViewModel
import nam.tran.android.helper.view.home.article.viewmodel.IArticleView
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mViewDataBinding?.viewModel = mViewModel

        var isRender = true

        val adapter = ArticleAdapter(dataBindingComponent, {
            isLoadAfter = true
        }, {
            isLoadBefore = true
        }, {
            isLoading = false
        }, {
            mViewModel?.posCurrent?.let {
                if (isRender && it != -1L) {
                    Logger.debug("Current Position - $it")
                    binding.rvArticle.layoutManager?.scrollToPosition(it.toInt())
                    isRender = false
                }
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
            adapter.add(it as List<ArticleModel>, type == AFTER)
        })

        mViewModel?.results?.observe(viewLifecycleOwner, Observer {
            if (it.initial) {
                mViewDataBinding?.viewModel = mViewModel
            } else {
                if (it.isSuccess()) {
                    if (it.data != null) {
                        when (it.data) {
                            1 -> isLoadAfter = false
                            2 -> isLoadBefore = false
                        }
                        isLoading = false
                    } else {
                        isLoadAfter = true
                        isLoadBefore = true
                    }
                }
                adapter.setNetworkState(it, type == AFTER)
            }
        })
    }

    override fun onStop() {
        super.onStop()
        binding.rvArticle.layoutManager?.let {
            mViewModel?.posCurrent = (it as LinearLayoutManager).findFirstCompletelyVisibleItemPosition().toLong()
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    enum class TYPE {
        AFTER, BEFORE
    }
}

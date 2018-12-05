package nam.tran.android.helper.view.home.article;

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import nam.tran.android.helper.R
import nam.tran.android.helper.databinding.FragmentArticleBinding
import nam.tran.android.helper.view.home.article.viewmodel.ArticleViewModel
import nam.tran.android.helper.view.home.article.viewmodel.IArticleView
import tran.nam.common.autoCleared
import tran.nam.core.biding.FragmentDataBindingComponent
import tran.nam.core.view.mvvm.BaseFragmentMVVM

class ArticleFragment : BaseFragmentMVVM<FragmentArticleBinding, ArticleViewModel>(), IArticleView {

    private val dataBindingComponent = FragmentDataBindingComponent(this)

    private var adapter by autoCleared<ArticleAdapter>()

    private var isLoading: Boolean = false
    private var mNumberToLoadMore = 1

    override fun initViewModel(factory: ViewModelProvider.Factory?) {
        mViewModel = ViewModelProviders.of(this, factory).get(ArticleViewModel::class.java)
    }

    override fun layoutId(): Int {
        return R.layout.fragment_article
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mViewDataBinding?.viewModel = mViewModel

        adapter = ArticleAdapter(dataBindingComponent)

        binding.rvArticle.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
//        binding.rvArticle.setOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                val layoutManager = recyclerView.layoutManager
//                layoutManager?.let {
//                    val totalItemCount = it.itemCount
//                    if (it is LinearLayoutManager) {
//                        val lastVisibleItemPosition = it.findLastVisibleItemPosition()
//
//                        if (!isLoading && totalItemCount <= lastVisibleItemPosition + mNumberToLoadMore) {
//                            val holder = recyclerView.findViewHolderForAdapterPosition(lastVisibleItemPosition)
//                            holder?.let {
//                                if (it is DataBoundViewHolder<*>){
//                                    if (it is AdapterArticleItemBinding){
//                                        mViewModel?.loadMore(it.article)
//                                        isLoading = true
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        })
        binding.rvArticle.adapter = adapter

        mViewModel?.results?.observe(viewLifecycleOwner, Observer {
            if (it?.data != null && it.data!!.isNotEmpty()) {
                adapter.replace(it.data!!)
            }
            mViewDataBinding?.viewModel = mViewModel
            mViewDataBinding?.executePendingBindings()
        })
    }
}

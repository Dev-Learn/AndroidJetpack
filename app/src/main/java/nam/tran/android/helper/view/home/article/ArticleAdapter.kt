package nam.tran.android.helper.view.home.article

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import nam.tran.android.helper.R
import nam.tran.android.helper.databinding.AdapterArticleItemBinding
import nam.tran.android.helper.model.ArticleModel
import tran.nam.common.DataBoundListAdapter

class ArticleAdapter(private val dataBindingComponent: DataBindingComponent) :
    DataBoundListAdapter<ArticleModel, AdapterArticleItemBinding>() {

    override fun createBinding(parent: ViewGroup): AdapterArticleItemBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.adapter_article_item,
            parent,
            false,
            dataBindingComponent
        )
    }

    override fun bind(binding: AdapterArticleItemBinding, item: ArticleModel) {
        binding.article = item
    }

    override fun areItemsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
        return oldItem.title == newItem.title
    }

}
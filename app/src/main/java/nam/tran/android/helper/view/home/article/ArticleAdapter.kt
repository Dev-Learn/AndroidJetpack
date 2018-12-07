package nam.tran.android.helper.view.home.article

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import nam.tran.android.helper.R
import nam.tran.android.helper.databinding.AdapterArticleItemBinding
import nam.tran.android.helper.databinding.NetworkStateItemBinding
import nam.tran.android.helper.model.ArticleModel
import nam.tran.domain.entity.state.Resource
import tran.nam.common.DataBoundListAdapter
import tran.nam.common.DataBoundViewHolder

class ArticleAdapter(private val dataBindingComponent: DataBindingComponent, private val loadBefore: () -> Unit) :
    DataBoundListAdapter<ArticleModel, ViewDataBinding>() {

    companion object {
        const val LIMIT = 100
    }

    private var networkState: Resource<*>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBoundViewHolder<ViewDataBinding> {
        return when (viewType) {
            R.layout.adapter_article_item -> {
                val binding = createBinding(parent)
                return DataBoundViewHolder(binding)
            }
            R.layout.network_state_item -> {
                val binding = createBindingNetwork(parent)
                return DataBoundViewHolder(binding)
            }
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: DataBoundViewHolder<ViewDataBinding>, position: Int) {

        when (getItemViewType(position)) {
            R.layout.adapter_article_item -> {
                bind(holder.binding, items!![position])
            }
            R.layout.network_state_item -> {
                if (holder.binding is NetworkStateItemBinding)
                    (holder.binding as NetworkStateItemBinding).resource = networkState
            }
        }
    }

    private fun hasExtraRow() = networkState != null && !networkState!!.isSuccess()

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1 || hasExtraRow() && position == 0) {
            R.layout.network_state_item
        } else {
            R.layout.adapter_article_item
        }
    }

    override fun createBinding(parent: ViewGroup): AdapterArticleItemBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.adapter_article_item,
            parent,
            false,
            dataBindingComponent
        )
    }

    private fun createBindingNetwork(parent: ViewGroup): NetworkStateItemBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.network_state_item,
            parent,
            false,
            dataBindingComponent
        )
    }

    override fun bind(binding: ViewDataBinding, item: ArticleModel) {
        if (binding is AdapterArticleItemBinding)
            binding.article = item
    }

    fun setNetworkState(newNetworkState: Resource<*>?, isAfter: Boolean = true) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (!hadExtraRow) {
                if (isAfter)
                    notifyItemInserted(itemCount)
                else
                    notifyItemInserted(0)
            }else{
                if (!isAfter)
                    notifyItemRemoved(0)
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }

    }

    override fun areItemsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
        return oldItem.title == newItem.title
    }

    fun isOverLimit(): Boolean {
        return itemCount == LIMIT
    }

    private fun isOverLimit(size: Int): Boolean {
        return size > LIMIT
    }

    fun add(data: List<ArticleModel>, isAfter: Boolean = true) {
        if (isAfter) {
            if (isOverLimit(itemCount + data.size)) {
                val surplus = itemCount + data.size - LIMIT
                val listItemRemove = ArrayList<ArticleModel>()
                for (i in 0..surplus - 1) {
                    items?.get(i)?.let {
                        listItemRemove.add(it)
                    }
                }
                items?.removeAll(listItemRemove)
                notifyItemRangeRemoved(0, surplus)
                loadBefore.invoke()
            }
            items?.addAll(data)
            notifyItemRangeInserted(itemCount, data.size)
        } else {
            if (isOverLimit(itemCount + data.size)) {
                val surplus = itemCount + data.size - LIMIT
                val size = itemCount - 1
                val listItemRemove = ArrayList<ArticleModel>()
                for (i in size downTo size - surplus + 1) {
                    items?.get(i)?.let {
                        listItemRemove.add(it)
                    }
                }
                items?.removeAll(listItemRemove)
                notifyItemRangeRemoved(itemCount, surplus)
            }
            for (i in 0..data.size - 1) {
                items?.add(i, data[i])
            }
            notifyItemRangeInserted(0, data.size)
        }

    }

    fun getItemLasted(): ArticleModel? {
        return items?.get(itemCount - 1)
    }

    fun getItemFirst(): ArticleModel? {
        return items?.get(0)
    }

}
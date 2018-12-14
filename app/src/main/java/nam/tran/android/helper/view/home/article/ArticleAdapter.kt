package nam.tran.android.helper.view.home.article

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import nam.tran.android.helper.R
import nam.tran.android.helper.databinding.AdapterArticleHeaderBinding
import nam.tran.android.helper.databinding.AdapterArticleItemBinding
import nam.tran.android.helper.databinding.NetworkStateItemBinding
import nam.tran.android.helper.model.ArticleModel
import nam.tran.domain.entity.state.Resource
import tran.nam.common.DataBoundViewHolder
import tran.nam.util.Constant.Companion.LIMIT
import tran.nam.util.Logger

class ArticleAdapter(
    private val dataBindingComponent: DataBindingComponent,
    private val loadAfter: () -> Unit,
    private val loadBefore: () -> Unit,
    private val loading: () -> Unit,
    private val rendered: () -> Unit
) : RecyclerView.Adapter<DataBoundViewHolder<ViewDataBinding>>() {

    private var items = ArrayList<ArticleModel>()

    private var networkState: Resource<*>? = null

    private var isAfter = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBoundViewHolder<ViewDataBinding> {
        return when (viewType) {
            R.layout.adapter_article_header -> {
                val binding = createBindingHeader(parent)
                DataBoundViewHolder(binding)
            }
            R.layout.adapter_article_item -> {
                val binding = createBinding(parent)
                DataBoundViewHolder(binding)
            }
            R.layout.network_state_item -> {
                val binding = createBindingNetwork(parent)
                DataBoundViewHolder(binding)
            }
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: DataBoundViewHolder<ViewDataBinding>, position: Int) {

        when (getItemViewType(position)) {
            R.layout.adapter_article_header -> {
                if (holder.binding is AdapterArticleHeaderBinding) {
                    (holder.binding as AdapterArticleHeaderBinding).tvDate.setText(items[position].headerValue)
                }
            }
            R.layout.adapter_article_item -> {
                if (position < items.size)
                    bind(holder.binding, items[position])
            }
            R.layout.network_state_item -> {
                if (holder.binding is NetworkStateItemBinding)
                    (holder.binding as NetworkStateItemBinding).resource = networkState
            }
        }
    }

    override fun getItemCount(): Int {
//        Logger.debug("SSSS - items.size 1:" + items.size)
        val size = items.size + if (hasExtraRow()) 1 else 0;
//        Logger.debug("SSSS - items.size 2:" + size)
        return size
    }

    private fun hasExtraRow() = networkState != null && !networkState!!.isSuccess()

    override fun getItemViewType(position: Int): Int {
        val additional = if (hasExtraRow() && !isAfter) 1 else 0
        val pos = position + additional
        return if (hasExtraRow() && ((position == itemCount - 1 && isAfter)|| (position == 0 && !isAfter))) {
            R.layout.network_state_item
        } else if (items[position - additional].isHeader) {
            R.layout.adapter_article_header
        } else {
            R.layout.adapter_article_item
        }
    }

    private fun createBinding(parent: ViewGroup): AdapterArticleItemBinding {
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

    private fun createBindingHeader(parent: ViewGroup): AdapterArticleHeaderBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.adapter_article_header,
            parent,
            false,
            dataBindingComponent
        )
    }

    fun bind(binding: ViewDataBinding, item: ArticleModel) {
        if (binding is AdapterArticleItemBinding)
            binding.article = item
    }

    fun setNetworkState(newNetworkState: Resource<*>?, isAfter: Boolean = true) {
        this.isAfter = isAfter
        Logger.debug("AA newNetworkState : " + newNetworkState.toString())
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            Logger.debug("AA  AAA")
            if (!hadExtraRow) {
                if (isAfter){
                    notifyItemInserted(itemCount)
                    Logger.debug("AA  1")
                } else {
                    notifyItemInserted(0)
                    Logger.debug("AA  2")
                }
            } else {
                if (!isAfter) {
                    notifyItemRemoved(0)
                    Logger.debug("AA  3")
                } else {
                    Logger.debug("AA  4")
                }
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            Logger.debug("AA  BBB")
            if (isAfter)
                notifyItemChanged(itemCount - 1)
            else {
                notifyItemChanged(0)
            }
        }
    }

    fun isOverLimit(): Boolean {
        Logger.debug("itemCount : ${items.size}")
        return items.size == LIMIT || items.size == LIMIT + 1
    }

    fun add(data: List<ArticleModel>, isAfter: Boolean = true) {
        if (isOverLimit()) {
            if (isAfter) loadBefore.invoke()
            else loadAfter.invoke()
        }
        updateData(data)
    }

    fun areItemsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
        return oldItem.id == newItem.id
    }

    fun areContentsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
        return oldItem.title == newItem.title
    }

    @SuppressLint("StaticFieldLeak")
    fun updateData(update: List<ArticleModel>) {
        val oldItems = items
        Logger.debug("Debug DiffUtil : items - " + items.size)
        object : AsyncTask<Void, Void, DiffUtil.DiffResult>() {
            override fun doInBackground(vararg voids: Void): DiffUtil.DiffResult {
                return DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                    override fun getOldListSize(): Int {
                        return oldItems.size
                    }

                    override fun getNewListSize(): Int {
                        return update.size
                    }

                    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                        val oldItem = oldItems[oldItemPosition]
                        val newItem = update[newItemPosition]
                        return areItemsTheSame(oldItem, newItem)
                    }

                    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                        val oldItem = oldItems[oldItemPosition]
                        val newItem = update[newItemPosition]
                        return areContentsTheSame(oldItem, newItem)
                    }
                })
            }

            override fun onPostExecute(diffResult: DiffUtil.DiffResult) {
                items = ArrayList(update)
                Logger.debug("Debug DiffUtil : items - " + items.size)
                diffResult.dispatchUpdatesTo(this@ArticleAdapter)
                loading.invoke()
                rendered.invoke()

            }
        }.execute()
    }

    fun getItemLasted(): ArticleModel? {
        return if (itemCount > 0) items[itemCount - 1] else null
    }

    fun getItemFirst(): ArticleModel? {
        return if (itemCount > 0) items[0] else null
    }

}
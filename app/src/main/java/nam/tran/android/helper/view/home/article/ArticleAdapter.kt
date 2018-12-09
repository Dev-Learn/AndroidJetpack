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
import nam.tran.android.helper.databinding.AdapterArticleItemBinding
import nam.tran.android.helper.databinding.NetworkStateItemBinding
import nam.tran.android.helper.model.ArticleModel
import nam.tran.domain.entity.state.Resource
import tran.nam.common.DataBoundViewHolder
import tran.nam.util.Constant.Companion.LIMIT

class ArticleAdapter(
    private val dataBindingComponent: DataBindingComponent,
    private val loadAfter: () -> Unit,
    private val loadBefore: () -> Unit,
    private val loading: () -> Unit
) :
    RecyclerView.Adapter<DataBoundViewHolder<ViewDataBinding>>() {

    private var items = ArrayList<ArticleModel>()

    private var networkState: Resource<*>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBoundViewHolder<ViewDataBinding> {
        return when (viewType) {
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
            R.layout.adapter_article_item -> {
                bind(holder.binding, items[position])
            }
            R.layout.network_state_item -> {
                if (holder.binding is NetworkStateItemBinding)
                    (holder.binding as NetworkStateItemBinding).resource = networkState
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size + if (hasExtraRow()) 1 else 0
    }

    private fun hasExtraRow() = networkState != null && !networkState!!.isSuccess()

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1 || hasExtraRow() && position == 0) {
            R.layout.network_state_item
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

    fun bind(binding: ViewDataBinding, item: ArticleModel) {
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
            if (isAfter)
                notifyItemRemoved(itemCount)
            else
                notifyItemRemoved(0)
        }

    }

    fun isOverLimit(): Boolean {
        return itemCount == LIMIT
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
                diffResult.dispatchUpdatesTo(this@ArticleAdapter)
                loading.invoke()
            }
        }.execute()
    }

    fun getItemLasted(): ArticleModel? {
        return items[itemCount - 1]
    }

    fun getItemFirst(): ArticleModel? {
        return items[0]
    }

}
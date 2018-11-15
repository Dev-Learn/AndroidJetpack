package nam.tran.android.helper.view.main.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import nam.tran.android.helper.R
import nam.tran.android.helper.databinding.AdapterLinkComicItemBinding
import nam.tran.android.helper.databinding.NetworkStateItemBinding
import nam.tran.android.helper.model.LinkComicModel
import nam.tran.domain.entity.state.NetworkState

class DetailComicAdapterPaging(
    private val dataBindingComponent: DataBindingComponent,
    private val retryCallback: () -> Unit
) : PagedListAdapter<LinkComicModel, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<LinkComicModel>() {
    override fun areItemsTheSame(p0: LinkComicModel, p1: LinkComicModel): Boolean {
        return p0.id == p1.id
    }

    override fun areContentsTheSame(p0: LinkComicModel, p1: LinkComicModel): Boolean {
        return false
    }
}) {

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.adapter_comic_item -> LinkComicItemViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.adapter_link_comic_item,
                    parent,
                    false,
                    dataBindingComponent
                )
            )
            R.layout.network_state_item -> {
                val holder = NetworkStateItemViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.network_state_item,
                        parent,
                        false,
                        dataBindingComponent
                    )
                )
                holder.binding.retryButton.setOnClickListener {
                    retryCallback()
                }
                return holder
            }
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.adapter_comic_item -> (holder as LinkComicItemViewHolder).bind(getItem(position))
            R.layout.network_state_item -> (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            val item = getItem(position)
            (holder as LinkComicItemViewHolder).bind(item)
        } else {
            onBindViewHolder(holder, position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.network_state_item
        } else {
            R.layout.adapter_comic_item
        }
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    class NetworkStateItemViewHolder(val binding: NetworkStateItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(state: NetworkState?) {
            binding.state = state
            binding.executePendingBindings()
        }
    }

    class LinkComicItemViewHolder(val binding: AdapterLinkComicItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(link: LinkComicModel?) {
            binding.link = link
            binding.executePendingBindings()
        }
    }

}
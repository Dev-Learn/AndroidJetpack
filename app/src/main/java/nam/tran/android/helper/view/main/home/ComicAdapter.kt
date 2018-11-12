package nam.tran.android.helper.view.main.home

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import nam.tran.android.helper.R
import nam.tran.android.helper.databinding.AdapterComicBinding
import nam.tran.android.helper.databinding.NetworkStateItemBinding
import nam.tran.android.helper.model.ComicModel
import nam.tran.domain.entity.state.NetworkState

class ComicAdapter :
    PagedListAdapter<ComicModel, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<ComicModel>() {
        override fun areItemsTheSame(p0: ComicModel, p1: ComicModel): Boolean {
            return p0.id == p1.id
        }

        override fun areContentsTheSame(p0: ComicModel, p1: ComicModel): Boolean {
            return p0.title == p1.title
        }
    }) {

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.adapter_comic_item -> ComicItemViewHolder(AdapterComicBinding.inflate(LayoutInflater.from(parent.context)))
            R.layout.network_state_item -> NetworkStateItemViewHolder(
                NetworkStateItemBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    )
                )
            )
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.adapter_comic_item -> (holder as ComicItemViewHolder).bind(getItem(position))
            R.layout.network_state_item -> (holder as NetworkStateItemViewHolder).bind(networkState)
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
        }
    }

    class ComicItemViewHolder(val binding: AdapterComicBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(comic: ComicModel?) {
            binding.comic = comic
        }
    }

}
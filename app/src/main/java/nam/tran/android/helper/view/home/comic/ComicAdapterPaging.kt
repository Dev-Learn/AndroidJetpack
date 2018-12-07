package nam.tran.android.helper.view.home.comic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import nam.tran.android.helper.R
import nam.tran.android.helper.databinding.AdapterComicItemBinding
import nam.tran.android.helper.databinding.NetworkStateItemBinding
import nam.tran.android.helper.model.ComicModel
import nam.tran.domain.entity.state.Resource
import tran.nam.util.Logger

class ComicAdapterPaging(
    private val dataBindingComponent: DataBindingComponent,
    private val like:(ComicModel?) -> Unit,
    private val goDetail:(ComicModel?) -> Unit
) :
    PagedListAdapter<ComicModel, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<ComicModel>() {
        override fun areItemsTheSame(p0: ComicModel, p1: ComicModel): Boolean {
            return p0.id == p1.id
        }

        override fun areContentsTheSame(p0: ComicModel, p1: ComicModel): Boolean {
            return false
        }
    }) {

    private var networkState: Resource<*>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.adapter_comic_item -> {
                val holder = ComicItemViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.adapter_comic_item,
                        parent,
                        false,
                        dataBindingComponent
                    ),goDetail
                )
                holder.binding.ckbLike.setOnCheckedChangeListener { buttonView, isChecked ->
                    run {
                        Logger.debug("setOnCheckedChangeListener : $isChecked")
                        holder.binding.comic?.isLike = isChecked
                        like(holder.binding.comic)
                    }
                }
                return holder
            }
            R.layout.network_state_item -> NetworkStateItemViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.network_state_item,
                    parent,
                    false,
                    dataBindingComponent
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

    private fun hasExtraRow() = networkState != null && !networkState!!.isSuccess()

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    fun setNetworkState(newNetworkState: Resource<*>?) {
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

        fun bind(state: Resource<*>?) {
            binding.resource = state
            binding.executePendingBindings()
        }
    }

    class ComicItemViewHolder(
        val binding: AdapterComicItemBinding,
        goDetail: (ComicModel?) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                goDetail(binding.comic)
            }
        }

        fun bind(comic: ComicModel?) {
            binding.comic = comic
            binding.executePendingBindings()
        }
    }

}
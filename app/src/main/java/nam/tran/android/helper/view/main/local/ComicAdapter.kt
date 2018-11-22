package nam.tran.android.helper.view.main.local

import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import nam.tran.android.helper.R
import nam.tran.android.helper.databinding.AdapterComicItemBinding
import nam.tran.android.helper.model.ComicModel
import tran.nam.common.DataBoundListAdapter

class ComicAdapter(private val dataBindingComponent: DataBindingComponent) :
    DataBoundListAdapter<ComicModel, AdapterComicItemBinding>() {

    override fun createBinding(parent: ViewGroup): AdapterComicItemBinding {
        val binding : AdapterComicItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.adapter_comic_item,
            parent,
            false,
            dataBindingComponent
        )
        binding.root.setOnClickListener {
            val bundle = bundleOf("comic" to binding.comic,"isLocal" to true)
            it.findNavController().navigate(R.id.action_localComicFragment_to_detailComicFragment, bundle)
        }
        return binding
    }

    override fun bind(binding: AdapterComicItemBinding, item: ComicModel) {
        binding.comic = item
        binding.ckbLike.visibility = View.GONE
    }

    override fun areItemsTheSame(oldItem: ComicModel, newItem: ComicModel): Boolean {
        return true
    }

    override fun areContentsTheSame(oldItem: ComicModel, newItem: ComicModel): Boolean {
        return true
    }

}
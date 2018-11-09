package nam.tran.android.helper.view.main.home

import android.databinding.DataBindingComponent
import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import nam.tran.android.helper.R
import nam.tran.android.helper.databinding.AdapterComicBinding
import nam.tran.android.helper.model.ComicModel
import tran.nam.common.DataBoundListAdapter

class ComicAdapter(private val dataBindingComponent: DataBindingComponent) :
    DataBoundListAdapter<ComicModel, AdapterComicBinding>() {

    override fun createBinding(parent: ViewGroup): AdapterComicBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.adapter_comic,
            parent,
            false,
            dataBindingComponent
        )
    }

    override fun bind(binding: AdapterComicBinding, item: ComicModel) {
        binding.comic = item
    }

    override fun areItemsTheSame(oldItem: ComicModel, newItem: ComicModel): Boolean {
        return true
    }

    override fun areContentsTheSame(oldItem: ComicModel, newItem: ComicModel): Boolean {
        return true
    }

}
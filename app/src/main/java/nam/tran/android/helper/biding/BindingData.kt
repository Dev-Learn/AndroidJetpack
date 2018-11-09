package nam.tran.android.helper.biding

import android.databinding.BindingAdapter
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import com.bumptech.glide.Glide
import nam.tran.android.helper.di.module.GlideApp
import nam.tran.android.helper.model.GenreModel

object BidingCommon {

    @JvmStatic
    @BindingAdapter("loadImageNetwork")
    fun loadImageNetwork(image : AppCompatImageView,urlImage : String){
        GlideApp.with(image).load(urlImage).centerCrop().into(image)
    }
}
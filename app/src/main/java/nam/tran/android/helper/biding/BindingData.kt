package nam.tran.android.helper.biding

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter

object BidingCommon {

    @JvmStatic
    @BindingAdapter("loadImageNetwork")
    fun loadImageNetwork(image: AppCompatImageView, urlImage: String) {
        GlideApp.with(image).load(urlImage).centerCrop().into(image)
    }
}
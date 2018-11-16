package nam.tran.android.helper.biding

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import nam.tran.android.helper.R
import nam.tran.android.helper.di.module.GlideApp

object BidingCommon {

    @JvmStatic
    @BindingAdapter("loadImageNetwork")
    fun loadImageNetwork(image: AppCompatImageView, urlImage: String) {
        val circularProgressDrawable = CircularProgressDrawable(image.context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        GlideApp.with(image).load(urlImage).diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(0.5f)
            .transition(DrawableTransitionOptions.withCrossFade()).placeholder(circularProgressDrawable)
            .error(R.drawable.image_error).into(image)
    }
}
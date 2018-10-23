package tran.nam.util


import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics

object Utils {

    val screenWidth: Int
        get() = Resources.getSystem().displayMetrics.widthPixels

    val screenHeight: Int
        get() = Resources.getSystem().displayMetrics.heightPixels

    fun scalePixel(context: Context): Float {
        val displayMetrics = context.resources.displayMetrics
        return (displayMetrics.widthPixels / 320).toFloat()
    }

    fun scaleDensity(context: Context): Float {
        val displayMetrics = context.resources.displayMetrics
        return displayMetrics.widthPixels.toFloat() / displayMetrics.density / 320f
    }

    fun dip2px(context: Context, dpVale: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpVale * scale + 0.5f).toInt()
    }
}

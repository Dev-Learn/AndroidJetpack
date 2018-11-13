package tran.nam.core.view

import android.app.Dialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import android.graphics.Point
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import android.view.*
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import tran.nam.core.R

@Suppress("MemberVisibilityCanBePrivate", "unused")
abstract class BaseDialog<T : ViewDataBinding> : androidx.fragment.app.DialogFragment() {

    protected abstract fun bidingData(biding: T)

    @LayoutRes
    protected abstract fun layoutID(): Int

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(cancelOnTouch())
        if (dialog.window != null) {
            dialog.window!!.attributes.windowAnimations = R.style.Dialog
        }
        return dialog
    }

    protected fun cancelOnTouch(): Boolean {
        return true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val biding = DataBindingUtil.inflate<T>(inflater,
                layoutID(), container, false)

        bidingData(biding)
        return biding.root
    }

    override fun onResume() {
        // Store access variables for window and blank point
        val window = dialog.window
        if (window != null) {
            val size = Point()
            // Store dimensions of the screen in `size`
            val display = window.windowManager.defaultDisplay
            display.getSize(size)
            // Set the width of the dialog proportional to 75% of the screen width
            if (maxWidth() != 0f && maxHeight() != 0f) {
                window.setLayout((size.x * maxWidth()).toInt(), (size.y * maxHeight()).toInt())
            } else if (maxWidth() != 0f) {
                window.setLayout((size.x * maxWidth()).toInt(), WRAP_CONTENT)
            } else if (maxHeight() != 0f) {
                window.setLayout(WRAP_CONTENT, (size.y * maxHeight()).toInt())
            } else {
                window.setLayout(WRAP_CONTENT, WRAP_CONTENT)
            }
            window.setGravity(Gravity.CENTER)
            // Call super onResume after sizing

        }
        super.onResume()
    }

    protected fun maxHeight(): Float {
        return 0f
    }

    protected fun maxWidth(): Float {
        return 0.6f
    }
}

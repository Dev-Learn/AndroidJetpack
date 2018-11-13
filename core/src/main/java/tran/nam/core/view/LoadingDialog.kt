package tran.nam.core.view

import android.app.Activity
import android.app.Dialog
import androidx.databinding.DataBindingUtil
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import tran.nam.core.R
import tran.nam.core.databinding.IncludeProgressLayoutBinding
import javax.inject.Inject

@Suppress("unused")
class LoadingDialog @Inject
internal constructor(activity: AppCompatActivity) {

    private var dialog: Dialog? = null
    private val mActivity: Activity

    fun isShowing(): Boolean {
        return dialog != null && dialog!!.isShowing
    }

    init {
        mActivity = activity

        dialog = Dialog(activity, R.style.LoadingTheme)

        val window = dialog!!.window
        if (window != null) {
            window.setGravity(Gravity.CENTER)
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)
            window.setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,
                    WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            window.attributes.dimAmount = 0.5f
        }
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setCancelable(false)
        dialog!!.setContentView(initLayout(activity))
    }

    private fun initLayout(activity: AppCompatActivity): View {
        val binding = DataBindingUtil.inflate<IncludeProgressLayoutBinding>(LayoutInflater.from(activity), R.layout.include_progress_layout, null, false)

        binding.progressLoading.visibility = View.VISIBLE
        return binding.root
    }

    fun showDialog() {
        try {
            if (!mActivity.isFinishing && !isShowing()) {
                dialog!!.show()
            }

        } catch (e: Exception) {
            Log.d("LoadingDialog", e.message)
        }

    }

    fun hideDialog() {
        try {
            dialog!!.dismiss()
        } catch (ignored: Exception) {
        }

    }

    fun cancelDialog() {
        if (dialog != null && dialog!!.isShowing) {
            try {
                dialog!!.cancel()
                dialog = null
            } catch (ignored: Exception) {
            }

        }
    }


}

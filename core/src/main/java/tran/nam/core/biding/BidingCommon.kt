@file:Suppress("unused")

package tran.nam.core.biding

import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import nam.tran.domain.entity.state.Loading
import nam.tran.domain.entity.state.Status
import tran.nam.core.view.BaseActivity
import tran.nam.core.viewmodel.IProgressViewModel
import tran.nam.core.viewmodel.IViewModel

object BidingCommon {

    @JvmStatic
    @BindingAdapter("visibleContainLoading")
    fun visibleContainLoading(view: View, iProgress: IProgressViewModel?) {
        val resource = iProgress?.resource()
        resource?.let {
            when (it.status) {
                Status.ERROR -> when (it.loading) {
                    Loading.LOADING_DIALOG -> {
                        view.visibility = View.GONE
                        dialogError(view, it.errorResource?.massage)
                    }
                    Loading.LOADING_NONE -> Toast.makeText(
                        view.context,
                        it.errorResource?.massage,
                        Toast.LENGTH_SHORT
                    ).show()
                    Loading.LOADING_NORMAL -> {
                        if (it.initial) {
                            view.visibility = View.VISIBLE
                        } else {
                            view.visibility = View.GONE
                        }
                    }
                }
                Status.LOADING -> when (it.loading) {
                    Loading.LOADING_DIALOG -> {
                        loadingDialog(view, true)
                    }
                    Loading.LOADING_NONE -> {
                    }
                    Loading.LOADING_NORMAL -> if (it.initial) view.visibility = View.VISIBLE
                }
                Status.SUCCESS -> when (it.loading) {
                    Loading.LOADING_DIALOG -> {
                        loadingDialog(view, false)
                    }
                    Loading.LOADING_NONE -> {
                    }
                    Loading.LOADING_NORMAL -> if (it.initial) view.visibility = View.GONE
                }
            }
        }
    }

    @JvmStatic
    @BindingAdapter("visibleProgress")
    fun visibleProgress(view: View, iProgress: IProgressViewModel?) {
        val resource = iProgress?.resource()
        resource?.let {
            when (it.status) {
                Status.ERROR, Status.SUCCESS -> view.visibility = View.GONE
                Status.LOADING -> when (it.loading) {
                    Loading.LOADING_DIALOG, Loading.LOADING_NONE -> {
                    }
                    Loading.LOADING_NORMAL -> view.visibility = View.VISIBLE
                }
            }
        }
    }

    @JvmStatic
    @BindingAdapter("visibleView")
    fun visibleView(view: View, iProgress: IProgressViewModel?) {
        val resource = iProgress?.resource()
        resource?.let {
            when (it.status) {
                Status.ERROR -> when (it.loading) {
                    Loading.LOADING_DIALOG, Loading.LOADING_NONE -> view.visibility = View.VISIBLE
                    Loading.LOADING_NORMAL -> if (it.initial) {
                        view.visibility = View.GONE
                    } else {
                        view.visibility = View.VISIBLE
                    }
                }
                Status.LOADING -> when (it.loading) {
                    Loading.LOADING_DIALOG, Loading.LOADING_NONE -> view.visibility = View.VISIBLE
                    Loading.LOADING_NORMAL -> if (it.initial) view.visibility = View.GONE
                }
                Status.SUCCESS -> view.visibility = View.VISIBLE
            }
        }
    }

    @JvmStatic
    @BindingAdapter("visibleTextError")
    fun visibleTextError(text: TextView, iProgress: IProgressViewModel?) {
        val resource = iProgress?.resource()
        resource?.let {
            when (it.status) {
                Status.ERROR -> when (it.loading) {
                    Loading.LOADING_DIALOG -> {
                    }
                    Loading.LOADING_NONE -> {
                    }
                    Loading.LOADING_NORMAL -> {
                        text.visibility = View.VISIBLE
                        text.text = it.errorResource?.massage
                    }
                }
                Status.LOADING, Status.SUCCESS -> text.visibility = View.GONE
            }
        }
    }

    @JvmStatic
    @BindingAdapter("visibleButtonError")
    fun visibleButtonError(bt: Button, iProgress: IProgressViewModel?) {
        val resource = iProgress?.resource()
        resource?.let {
            when (it.status) {
                Status.ERROR -> {
                    bt.visibility = View.VISIBLE
                    bt.setOnClickListener {
                        resource.retry?.invoke()
                    }
                }
                Status.LOADING, Status.SUCCESS -> bt.visibility = View.GONE
            }
        }
    }

    private fun loadingDialog(view: View, isShow: Boolean?) {
        val context = view.context
        if (context is IViewModel) {
            if (isShow!!) {
                context.showDialogLoading()
            } else {
                context.hideDialogLoading()
            }
        } else {
            if (context is BaseActivity) {
                val manager = context.supportFragmentManager?.primaryNavigationFragment
                if (manager != null) {
                    val fragment = manager.childFragmentManager.primaryNavigationFragment
                    if (fragment != null && fragment is IViewModel) {
                        if (isShow!!) {
                            fragment.showDialogLoading()
                        } else {
                            fragment.hideDialogLoading()
                        }
                    }
                }
            }
        }
    }

    private fun dialogError(view: View, error: String?) {
        val context = view.context
        if (context is IViewModel) {
            context.onShowDialogError(error)
        } else {
            if (context is BaseActivity) {
                val manager = context.supportFragmentManager?.primaryNavigationFragment
                if (manager != null) {
                    val fragment = manager.childFragmentManager.primaryNavigationFragment
                    if (fragment != null && fragment is IViewModel) {
                        fragment.onShowDialogError(error)
                    }
                }
            }
        }
    }
}

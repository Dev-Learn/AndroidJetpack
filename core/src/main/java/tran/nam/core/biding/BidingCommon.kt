@file:Suppress("unused")

package tran.nam.core.biding

import android.databinding.BindingAdapter
import android.view.View
import android.widget.TextView
import android.widget.Toast
import nam.tran.domain.entity.state.Loading
import nam.tran.domain.entity.state.Status
import tran.nam.core.viewmodel.IProgressViewModel

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
//                        dialogError(view, it.message)
                    }
                    Loading.LOADING_NONE -> Toast.makeText(view.context, it.message, Toast.LENGTH_SHORT).show()
                    Loading.LOADING_NORMAL -> {
                    }
                }
                Status.LOADING -> when (it.loading) {
                    Loading.LOADING_DIALOG -> {/*loadingDialog(view, true)*/
                    }
                    Loading.LOADING_NONE -> {
                    }
                    Loading.LOADING_NORMAL -> view.visibility = View.VISIBLE
                }
                Status.SUCCESS -> when (it.loading) {
                    Loading.LOADING_DIALOG -> {/*loadingDialog(view, false)*/
                    }
                    Loading.LOADING_NONE -> {
                    }
                    Loading.LOADING_NORMAL -> view.visibility = View.GONE
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
    @BindingAdapter("visibleError")
    fun visibleError(textView: TextView, iProgress: IProgressViewModel?) {
        val resource = iProgress?.resource()
        resource?.let {
            when (it.status) {
                Status.ERROR -> when (it.loading) {
                    Loading.LOADING_DIALOG -> {
                    }
                    Loading.LOADING_NONE -> {
                    }
                    Loading.LOADING_NORMAL -> {
                        textView.visibility = View.VISIBLE
                        textView.text = it.message
                    }
                }
                Status.LOADING, Status.SUCCESS -> textView.visibility = View.GONE
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
                    Loading.LOADING_NORMAL -> view.visibility = View.GONE
                }
                Status.LOADING -> when (it.loading) {
                    Loading.LOADING_DIALOG, Loading.LOADING_NONE -> view.visibility = View.VISIBLE
                    Loading.LOADING_NORMAL -> view.visibility = View.GONE
                }
                Status.SUCCESS -> view.visibility = View.VISIBLE
            }
        }
    }

    @JvmStatic
    @BindingAdapter("textError")
    fun textError(text: TextView, iProgress: IProgressViewModel?) {
        val resource = iProgress?.resource()
        resource?.let {
            when (it.status) {
                Status.ERROR -> text.text = it.message
                Status.LOADING, Status.SUCCESS -> {
                }
            }
        }
    }

//    private fun loadingDialog(view: View, isShow: Boolean?) {
//        val context = view.context
//        if (context is IViewModel) {
//            if (isShow!!) {
//                (context as IViewModel).showDialogLoading()
//            } else {
//                (context as IViewModel).hideDialogLoading()
//            }
//        } else {
//            if (context is BaseActivityWithFragment) {
//                val fragmentHelper = context.mFragmentHelper
//                val fragment = fragmentHelper?.getCurrentFragment()
//                if (fragment != null && fragment is BaseParentFragment) {
//                    val fragmentHelperChild = fragment.mChildFragmentHelper
//                    val childFragment = fragmentHelperChild.getCurrentFragment()
//                    if (childFragment is IViewModel) {
//                        if (isShow!!) {
//                            (childFragment as IViewModel).showDialogLoading()
//                        } else {
//                            (childFragment as IViewModel).hideDialogLoading()
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private fun dialogError(view: View, error: String?) {
//        val context = view.context
//        if (context is IViewModel) {
//            (context as IViewModel).onShowDialogError(error)
//        } else {
//            if (context is BaseActivityWithFragment) {
//                val fragmentHelper = context.mFragmentHelper
//                val fragment = fragmentHelper?.getCurrentFragment()
//                if (fragment != null && fragment is BaseParentFragment) {
//                    val fragmentHelperChild = fragment.mChildFragmentHelper
//                    val childFragment = fragmentHelperChild.getCurrentFragment()
//                    if (childFragment is IViewModel) {
//                        (childFragment as IViewModel).onShowDialogError(error)
//                    }
//                }
//            }
//        }
//    }
}

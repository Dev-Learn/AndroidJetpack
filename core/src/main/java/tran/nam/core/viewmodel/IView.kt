package tran.nam.core.viewmodel

import androidx.lifecycle.LifecycleOwner

interface IView : LifecycleOwner {

    fun showDialogLoading()

    fun hideDialogLoading()

    fun onShowDialogError(message: String?, codeError: Int?)
}

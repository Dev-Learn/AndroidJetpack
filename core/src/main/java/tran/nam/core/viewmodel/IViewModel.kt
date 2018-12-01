package tran.nam.core.viewmodel

import androidx.lifecycle.LifecycleOwner

interface IViewModel : LifecycleOwner {

    fun showDialogLoading()

    fun hideDialogLoading()

    fun onShowDialogError(message: String?, codeError: Int?)
}

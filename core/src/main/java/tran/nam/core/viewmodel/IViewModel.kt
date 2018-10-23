package tran.nam.core.viewmodel

import android.arch.lifecycle.LifecycleOwner

import nam.tran.domain.entity.state.Loading
import nam.tran.domain.entity.state.Status

interface IViewModel : LifecycleOwner {

    fun showDialogLoading()

    fun hideDialogLoading()

    fun onShowDialogError(message: String?)
}

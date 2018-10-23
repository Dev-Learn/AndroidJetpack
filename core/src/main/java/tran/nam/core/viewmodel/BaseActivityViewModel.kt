package tran.nam.core.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.os.Bundle
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference

@Suppress("unused", "UNUSED_PARAMETER")
open class BaseActivityViewModel(application: Application) : AndroidViewModel(application), LifecycleObserver {

    @Volatile
    var mViewWeakReference: WeakReference<IViewModel>? = null

    private var compositeDisposables: CompositeDisposable? = null

    protected inline fun<reified V: IViewModel> view(): V? {
        if (mViewWeakReference == null || mViewWeakReference?.get() == null)
            return null
        return V::class.java.cast(mViewWeakReference?.get())
    }

    open fun onCreated(view: IViewModel) {
        mViewWeakReference = WeakReference(view)
        if (compositeDisposables == null)
            compositeDisposables = CompositeDisposable()
        view.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun create() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun start() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun resume() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    open fun pause() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun stop() {
    }

    open fun onRestoreInstanceState(savedInstanceState: Bundle?) {

    }

    open fun onSaveInstanceState(outState: Bundle?) {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun cleanup() {
        val viewWeakReference = this.mViewWeakReference
        if (viewWeakReference != null) {
            val view = viewWeakReference.get()
            view?.lifecycle?.removeObserver(this)
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (compositeDisposables != null)
            compositeDisposables!!.dispose()
    }

    open fun addDisposable(disposable: Disposable) {
        compositeDisposables!!.add(disposable)
    }
}

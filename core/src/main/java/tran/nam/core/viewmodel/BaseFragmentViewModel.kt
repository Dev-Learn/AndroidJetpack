package tran.nam.core.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference

open class BaseFragmentViewModel(application: Application) : AndroidViewModel(application), LifecycleObserver {

    @Volatile
    var mViewWeakReference: WeakReference<IViewModel>? = null

    private var compositeDisposables: CompositeDisposable? = null

    protected inline fun<reified V: IViewModel> view(): V? {
        if (mViewWeakReference == null || mViewWeakReference?.get() == null)
            return null
        return V::class.java.cast(mViewWeakReference?.get())
    }

    fun onAttach(view: IViewModel) {
        mViewWeakReference = WeakReference(view)
        view.lifecycle.addObserver(this)
    }

    open fun onInitialized() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreated() {
        if (compositeDisposables == null)
            compositeDisposables = CompositeDisposable()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onStart() {
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun onResume() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    open fun onPause() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun onStop() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    internal fun onDestroy() {
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

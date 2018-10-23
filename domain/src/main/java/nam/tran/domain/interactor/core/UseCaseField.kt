package nam.tran.domain.interactor.core

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.subscribers.DisposableSubscriber
import nam.tran.domain.IRepository
import nam.tran.domain.executor.SchedulerProvider

/**
 * Abstract class for a Use Case Field(Interactor in terms of Clean Architecture).
 * This interface represents a execution unit for different use cases (this means any use case
 * in the application should implement this contract).
 *
 *
 * By convention each UseCase implementation will return the result using a [DisposableObserver]
 * that will execute its job in a background thread and will post the result in the UI thread.
 */
abstract class UseCaseField<T, Params>(protected var iRepository: IRepository, private val schedulerProvider: SchedulerProvider) {

    /**
     * Builds an [Flowable] which will be used when executing the current [UseCase].
     */
    protected abstract fun buildUseCaseFlowable(params: Params): Flowable<T>

    /**
     * Builds an [Flowable] which will be used when executing the current [UseCase].
     */
    protected abstract fun buildUseCaseObserve(params: Params): Observable<T>

    /**
     * Builds an [Completable] which will be used when executing the current [UseCase].
     */
    protected abstract fun buildUseCaseCompletable(params: Params): Completable

    /**
     * Executes the current use case.
     *
     * @param subscriber [DisposableSubscriber] which will be listening to the observable build
     * by [.buildUseCaseFlowable] ()} method.
     * @param params     Parameters (Optional) used to build/execute this use case.
     */
    fun execute(subscriber: DisposableSubscriber<T>, params: Params, isSubscribeAndObserve: Boolean): Disposable {
        val flowable = this.buildUseCaseFlowable(params)
        if (isSubscribeAndObserve)
            flowable.subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
        return flowable.subscribeWith(subscriber)
    }

    /**
     * Executes the current use case.
     *
     * @param observer [DisposableObserver] which will be listening to the observable build
     * by [.buildUseCaseObserve] ()} method.
     * @param params   Parameters (Optional) used to build/execute this use case.
     */
    fun execute(observer: DisposableObserver<T>, params: Params, isSubscribeAndObserve: Boolean): Disposable {
        val observable = this.buildUseCaseObserve(params);
        if (isSubscribeAndObserve)
            observable.subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
        return observable.subscribeWith(observer)
    }

    /**
     * Executes the current use case.
     *
     * @param subscriber [DisposableSubscriber] which will be listening to the observable build
     * by [.buildUseCaseFlowable] ()} method.
     * @param params     Parameters (Optional) used to build/execute this use case.
     */
    fun execute(subscriber: DisposableCompletableObserver, params: Params, isSubscribeAndObserve: Boolean): Disposable {
        val completable = this.buildUseCaseCompletable(params)
        if (isSubscribeAndObserve)
            completable.subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
        return completable.subscribeWith(subscriber)
    }
}

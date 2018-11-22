//package tran.nam.core.view
//
//import java.lang.ref.WeakReference
//
//class WaitThread(fragment: BaseFragment) : Thread() {
//
//    private var fragmentWeak: WeakReference<BaseFragment>? = null
//    private var isStopped: Boolean = false
//    private val mObject = Object()
//
//    init {
//        fragmentWeak = WeakReference(fragment)
//    }
//
//    override fun run() {
//        val fragment = fragmentWeak?.get()
//        fragment?.let { it ->
//            while (!it.isViewCreated && !isStopped) {
//                try {
//                    synchronized(mObject) {
//                        mObject.wait()
//                    }
//                } catch (e: InterruptedException) {
//                    e.printStackTrace()
//                }
//
//            }
//
//            if (isStopped)
//                return
//
//            val finalFragment = fragmentWeak?.get()
//            finalFragment?.let {
//                it.activity!!.runOnUiThread {
//                    if (it.isInitialized)
//                        it.initialized()
//                    else
//                        it.handleAfterVisible()
//                }
//            }
//        }
//    }
//
//    fun continueProcessing() {
//        synchronized(mObject) {
//            mObject.notifyAll()
//        }
//    }
//
//    fun stopProcessing() {
//        isStopped = true
//        synchronized(mObject) {
//            mObject.notifyAll()
//        }
//    }
//}
//

package tran.nam.core.view

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.Navigation

abstract class BaseActivity : AppCompatActivity() {

    companion object {

        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    private var mLoadingDialog: LoadingDialog? = null

    open val navController by lazy {
        Navigation.findNavController(this, navigationId())
    }

    /**
     * @return layout resource id
     */
    @LayoutRes
    abstract fun layoutId(): Int

    @IdRes
    abstract fun navigationId(): Int

    protected open fun setStatusBar() {}

    /*
     * Init Fragment Helper
     **/
    protected open fun initFragment() {}

    protected open fun initLayout() {
        setContentView(layoutId())
    }

    /*
     * Init injection for activity
     **/
    protected open fun inject() {}

    open fun initData(savedInstanceState: Bundle?) {}


    override fun onCreate(savedInstanceState: Bundle?) {
        setStatusBar()
        inject()
        super.onCreate(savedInstanceState)
        initLayout()
        initFragment()
        mLoadingDialog = LoadingDialog(this)
        initData(savedInstanceState)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    override fun onBackPressed() {
        if (!navController.popBackStack()) {
            super.onBackPressed()
        }
    }

    fun showLoadingDialog() {
        if (mLoadingDialog != null && !mLoadingDialog!!.isShowing()) {
            mLoadingDialog!!.showDialog()
        }
    }

    fun hideLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog!!.isShowing()) {
            mLoadingDialog!!.hideDialog()
        }
    }

    fun hideKeyboard() {
        val view = currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun showKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        mLoadingDialog?.cancelDialog()
    }
}
package nam.tran.android.helper.view.main

import android.os.Bundle
import androidx.navigation.Navigation
import nam.tran.android.helper.R
import tran.nam.core.view.BaseActivityInjection

class MainActivity : BaseActivityInjection() {

    override fun layoutId(): Int {
        return R.layout.activity_main
    }

    private val navController by lazy {
        Navigation.findNavController(this, R.id.nav_host_fragment)
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    override fun onBackPressed() {
        if (!navController.popBackStack()) {
            super.onBackPressed()
        }
    }
}

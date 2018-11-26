package tran.nam.core.view

import androidx.annotation.IdRes
import androidx.navigation.Navigation

abstract class BaseActivityNavigation : BaseActivity(){

    @IdRes
    abstract fun navigationId(): Int

    open val navController by lazy {
        Navigation.findNavController(this, navigationId())
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
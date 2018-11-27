package nam.tran.android.helper.view.main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import nam.tran.android.helper.R
import nam.tran.android.helper.databinding.ActivityMainBinding
import tran.nam.core.view.BaseActivityInjection

class MainActivity : BaseActivityInjection() {

    val navController by lazy {
        Navigation.findNavController(this, R.id.nav_host_fragment)
    }

    override fun layoutId(): Int {
        return R.layout.activity_main
    }

    override fun init(savedInstanceState: Bundle?) {
        val inflater = navController.navInflater
        val graph = inflater.inflate(R.navigation.nav_graph)
        graph.setDefaultArguments(intent.extras)
        if (intent.hasExtra("isLogin")) {
            graph.startDestination = R.id.homeFragment
        } else {
            graph.startDestination = R.id.loginFragment
        }
        navController.graph = graph
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

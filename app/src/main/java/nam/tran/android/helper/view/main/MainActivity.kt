package nam.tran.android.helper.view.main

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import nam.tran.android.helper.R
import nam.tran.android.helper.databinding.ActivityMainBinding
import nam.tran.android.helper.view.main.viewmodel.IMainViewModel
import nam.tran.android.helper.view.main.viewmodel.MainViewModel
import tran.nam.core.view.mvvm.BaseActivityMVVM

class MainActivity : BaseActivityMVVM<ActivityMainBinding, MainViewModel>(), IMainViewModel {

    val navController by lazy {
        Navigation.findNavController(this, R.id.nav_host_fragment)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    override fun onBackPressed() {
        if (!navController.popBackStack()) {
            super.onBackPressed()
        }
    }

    override fun layoutId(): Int {
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(MainViewModel::class.java)
        return R.layout.activity_main
    }

    override fun init(savedInstanceState: Bundle?) {
        mViewDataBinding.viewModel = mViewModel

        val inflater = navController.navInflater
        val graph = inflater.inflate(R.navigation.nav_graph)
        graph.setDefaultArguments(intent.extras)
        mViewModel?.let {
            if (it.isLogin()) {
                graph.startDestination = R.id.homeFragment
            } else {
                graph.startDestination = R.id.loginFragment
            }
            navController.graph = graph
        }


    }

}

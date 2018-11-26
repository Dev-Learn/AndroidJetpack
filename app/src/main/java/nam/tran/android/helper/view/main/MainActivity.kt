package nam.tran.android.helper.view.main

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import nam.tran.android.helper.R
import nam.tran.android.helper.databinding.ActivityMainBinding
import nam.tran.android.helper.view.main.viewmodel.IMainViewModel
import nam.tran.android.helper.view.main.viewmodel.MainViewModel
import tran.nam.core.view.mvvm.BaseActivityNavigationMVVM

class MainActivity : BaseActivityNavigationMVVM<ActivityMainBinding, MainViewModel>(), IMainViewModel {

    override fun navigationId(): Int {
        return R.id.nav_host_fragment
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

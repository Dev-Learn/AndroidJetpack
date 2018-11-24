package nam.tran.android.helper.view.splash;

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import nam.tran.android.helper.R
import nam.tran.android.helper.databinding.FragmentSplashBinding
import nam.tran.android.helper.view.splash.viewmodel.ISplashViewModel
import nam.tran.android.helper.view.splash.viewmodel.SplashViewModel
import tran.nam.core.view.mvvm.BaseFragmentMVVM
import tran.nam.util.Logger

class SplashFragment : BaseFragmentMVVM<FragmentSplashBinding, SplashViewModel>(), ISplashViewModel {

    override fun initViewModel(factory: ViewModelProvider.Factory?) {
        mViewModel = ViewModelProviders.of(this, factory).get(SplashViewModel::class.java)
    }

    override fun layoutId(): Int {
        return R.layout.fragment_splash
    }

    override fun onVisible() {
        mViewDataBinding.viewModel = mViewModel
        mViewDataBinding.image.postDelayed(runnable, 2000)
    }

    var runnable = Runnable {
        mViewModel?.let {
            // todo : Error when app go to background ( navigate not call - Ignoring navigate() call)
            try {
                if (it.isLogin()) {
                    mViewDataBinding.image.findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                } else {
                    mViewDataBinding.image.findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                }
            } catch (e: Exception) {
                Logger.debug(e)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewDataBinding.image.removeCallbacks(runnable)
    }
}

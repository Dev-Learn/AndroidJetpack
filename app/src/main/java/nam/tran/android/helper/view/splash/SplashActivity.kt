package nam.tran.android.helper.view.splash

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import nam.tran.android.helper.R
import nam.tran.android.helper.databinding.ActivitySplashBinding
import nam.tran.android.helper.view.main.MainActivity
import nam.tran.android.helper.view.splash.viewmodel.ISplashView
import nam.tran.android.helper.view.splash.viewmodel.SplashViewModel
import tran.nam.core.view.mvvm.BaseActivityMVVM

class SplashActivity : BaseActivityMVVM<ActivitySplashBinding, SplashViewModel>(), ISplashView {

    override fun layoutId(): Int {
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(SplashViewModel::class.java)
        return R.layout.activity_splash
    }

    var runnable = Runnable {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("isLogin",mViewModel?.isLogin())
        startActivity(intent)
        finish()
        overridePendingTransition(tran.nam.core.R.anim.slide_right_in, tran.nam.core.R.anim.slide_left_out)
    }

    override fun init(savedInstanceState: Bundle?) {
        mViewDataBinding.image.postDelayed(runnable, 2000)
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewDataBinding.image.removeCallbacks(runnable)
    }
}

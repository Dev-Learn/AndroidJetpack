package nam.tran.android.helper.view.splash

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import nam.tran.android.helper.view.main.MainActivity
import nam.tran.android.helper.R
import nam.tran.android.helper.databinding.ActivitySplashBinding
import tran.nam.core.view.BaseActivity

class SplashActivity : BaseActivity() {

    private lateinit var mViewDataBinding: ActivitySplashBinding

    override fun layoutId(): Int {
        return R.layout.activity_splash
    }

    var runnable = Runnable {
        if (!this.isDestroyed){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            overridePendingTransition(tran.nam.core.R.anim.slide_right_in, tran.nam.core.R.anim.slide_left_out);
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        mViewDataBinding = DataBindingUtil.setContentView(this, layoutId())
        mViewDataBinding.view = this


    }

    override fun initData(savedInstanceState: Bundle?) {
        mViewDataBinding.image.postDelayed(runnable, 2000)
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewDataBinding.image.removeCallbacks(runnable)
    }

}

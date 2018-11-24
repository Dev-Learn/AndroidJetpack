package nam.tran.android.helper.view

import nam.tran.android.helper.R
import tran.nam.core.view.BaseActivityInjection

class MainActivity : BaseActivityInjection() {

    override fun layoutId(): Int {
        return R.layout.activity_main
    }

    override fun navigationId(): Int {
        return R.id.nav_host_fragment
    }


}

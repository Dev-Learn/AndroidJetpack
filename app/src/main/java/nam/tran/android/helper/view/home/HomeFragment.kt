package nam.tran.android.helper.view.home


import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.fragment_home.*
import nam.tran.android.helper.R
import tran.nam.core.view.BaseFragmentInjection

class HomeFragment : BaseFragmentInjection() {

    lateinit var navController : NavController

    public override fun layoutId(): Int {
        return R.layout.fragment_home
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view.findViewById<View>(R.id.nav_fragment_home))

        NavigationUI.setupWithNavController(
            nav_bottom,navController
        )
    }
}

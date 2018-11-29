package nam.tran.android.helper.view.home.user;

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import nam.tran.android.helper.R
import nam.tran.android.helper.databinding.FragmentUserBinding
import nam.tran.android.helper.view.home.user.viewmodel.IUserViewModel
import nam.tran.android.helper.view.home.user.viewmodel.UserViewModel
import tran.nam.core.view.mvvm.BaseFragmentMVVM

class UserFragment : BaseFragmentMVVM<FragmentUserBinding, UserViewModel>(), IUserViewModel {

    override fun initViewModel(factory: ViewModelProvider.Factory?) {
        mViewModel = ViewModelProviders.of(this, factory).get(UserViewModel::class.java)
    }

    override fun layoutId(): Int {
        return R.layout.fragment_user
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding.viewModel = mViewModel
        mViewDataBinding.view = this
    }

    fun logout() {
        mViewModel?.logout()
        Navigation.findNavController(requireActivity().findViewById<View>(R.id.nav_host_fragment)).navigate(R.id.action_homeFragment_to_loginFragment)
    }
}

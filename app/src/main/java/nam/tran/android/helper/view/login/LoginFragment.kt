package nam.tran.android.helper.view.login;


import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import nam.tran.android.helper.R
import nam.tran.android.helper.databinding.FragmentLoginBinding
import nam.tran.android.helper.view.login.viewmodel.ILoginViewModel
import nam.tran.android.helper.view.login.viewmodel.LoginViewModel

import tran.nam.core.view.mvvm.BaseFragmentMVVM

class LoginFragment : BaseFragmentMVVM<FragmentLoginBinding, LoginViewModel>(),
    ILoginViewModel {

    override fun initViewModel(factory: ViewModelProvider.Factory?) {
        mViewModel = ViewModelProviders.of(this, factory).get(LoginViewModel::class.java)
    }

    override fun layoutId(): Int {
        return R.layout.fragment_login
    }

    override fun onVisible() {
        mViewDataBinding.viewModel = mViewModel
        mViewDataBinding.view = this

        mViewModel?.results?.observe(this, Observer {
            mViewDataBinding.viewModel = mViewModel
            mViewDataBinding.executePendingBindings()
            if (it.isSuccess()) {
                mViewDataBinding.view?.findNavController()?.navigate(R.id.action_loginFragment_to_homeFragment)
            }
        })
    }

    fun register(v: View) {
        v.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }

    fun forgotPassword() {

    }
}

package nam.tran.android.helper.view.register;

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import nam.tran.android.helper.R
import nam.tran.android.helper.databinding.FragmentRegisterBinding
import nam.tran.android.helper.view.register.viewmodel.IRegisterViewModel
import nam.tran.android.helper.view.register.viewmodel.RegisterViewModel
import tran.nam.core.view.mvvm.BaseFragmentMVVM

class RegisterFragment : BaseFragmentMVVM<FragmentRegisterBinding, RegisterViewModel>(),
    IRegisterViewModel {

    companion object {
        @JvmStatic
        fun newInstance(): RegisterFragment =
            RegisterFragment()
    }

    override fun initViewModel(factory: ViewModelProvider.Factory?) {
        mViewModel = ViewModelProviders.of(this, factory).get(RegisterViewModel::class.java)
    }

    override fun layoutId(): Int {
        return R.layout.fragment_register
    }

    override fun onVisible() {
        mViewDataBinding.viewModel = mViewModel
    }
}

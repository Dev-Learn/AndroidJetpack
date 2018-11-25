package nam.tran.android.helper.view.register;

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import nam.tran.android.helper.R
import nam.tran.android.helper.databinding.FragmentRegisterBinding
import nam.tran.android.helper.view.register.viewmodel.IRegisterViewModel
import nam.tran.android.helper.view.register.viewmodel.RegisterViewModel
import tran.nam.core.view.mvvm.BaseFragmentMVVM

class RegisterFragment : BaseFragmentMVVM<FragmentRegisterBinding, RegisterViewModel>(),
    IRegisterViewModel {

    override fun initViewModel(factory: ViewModelProvider.Factory?) {
        mViewModel = ViewModelProviders.of(this, factory).get(RegisterViewModel::class.java)
    }

    override fun layoutId(): Int {
        return R.layout.fragment_register
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding.viewModel = mViewModel

        mViewModel?.results?.observe(this, Observer { result ->
            result?.let {
                mViewDataBinding.viewModel = mViewModel
                if (it.isSuccess()) {
                    val alarm = AlertDialog.Builder(context!!)
                    alarm.setTitle("Success")
                    alarm.setMessage(it.data)
                    alarm.setCancelable(false)
                    alarm.setPositiveButton("Ok") { dialog, which ->
                        mViewDataBinding.root.findNavController().navigateUp()
                    }
                    alarm.show()
                }
            }
        })
    }
}

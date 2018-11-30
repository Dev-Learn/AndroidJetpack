package nam.tran.android.helper.view.forgotPassword

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import nam.tran.android.helper.R
import nam.tran.android.helper.databinding.FragmentForgotPasswordBinding
import nam.tran.android.helper.view.forgotPassword.viewmodel.ForgotPasswordViewModel
import nam.tran.android.helper.view.forgotPassword.viewmodel.IForgotPasswordViewModel
import tran.nam.core.view.mvvm.BaseFragmentMVVM

class ForgotPasswordFragment : BaseFragmentMVVM<FragmentForgotPasswordBinding, ForgotPasswordViewModel>(),
    IForgotPasswordViewModel {

    override fun initViewModel(factory: ViewModelProvider.Factory?) {
        mViewModel = ViewModelProviders.of(this, factory).get(ForgotPasswordViewModel::class.java)
    }

    override fun layoutId(): Int {
        return R.layout.fragment_forgot_password
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding?.viewModel = mViewModel

        mViewModel?.results?.observe(this, Observer {
            mViewDataBinding?.viewModel = mViewModel
            if (it.isSuccess()){
                val alarm = AlertDialog.Builder(context!!)
                alarm.setTitle("Success")
                alarm.setMessage(it.data)
                alarm.setCancelable(false)
                alarm.setPositiveButton("OK") { dialog, which ->
                    mViewDataBinding?.root?.findNavController()?.navigateUp()
                }
                alarm.show()
            }
        })
    }
}

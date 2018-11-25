package nam.tran.android.helper.view.login;


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import nam.tran.android.helper.R
import nam.tran.android.helper.databinding.FragmentLoginBinding
import nam.tran.android.helper.view.login.viewmodel.ILoginViewModel
import nam.tran.android.helper.view.login.viewmodel.LoginViewModel
import nam.tran.domain.entity.state.Resource
import tran.nam.core.view.mvvm.BaseFragmentMVVM
import tran.nam.util.Logger

class LoginFragment : BaseFragmentMVVM<FragmentLoginBinding, LoginViewModel>(),
    ILoginViewModel, Observer<Resource<Void>?> {

    override fun initViewModel(factory: ViewModelProvider.Factory?) {
        mViewModel = ViewModelProviders.of(this, factory).get(LoginViewModel::class.java)
    }

    override fun layoutId(): Int {
        return R.layout.fragment_login
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding.viewModel = mViewModel
        mViewDataBinding.view = this

        mViewModel?.results?.removeObserver(this)

        mViewModel?.onCreated()

        mViewModel?.results?.observe(this, this)
    }

    override fun onChanged(result: Resource<Void>?) {
        Logger.debug(result)
        result?.let {
            mViewDataBinding.viewModel = mViewModel

            if (mViewModel?.type == LoginViewModel.TYPE.LOGIN) {
                login(it)
            } else {
                verifyEmail(it)
            }
        }
    }

    private fun verifyEmail(it: Resource<Void>) {
        if (it.isSuccess()) {
            Toast.makeText(context, "Please check email to verify account", Toast.LENGTH_SHORT).show()
        }
    }

    private fun login(it: Resource<Void>) {
        if (it.isError()) {
            if (it.getStatusCode() == 600) {
                val alarm = AlertDialog.Builder(context!!)
                alarm.setTitle(it.getMassageError())
                alarm.setMessage("You want resend email verify ???")
                alarm.setCancelable(false)
                alarm.setPositiveButton("Ok") { dialog, which ->
                    dialog.dismiss()
                    mViewModel?.resendVerifyEmail(
                        mViewDataBinding.edtEmail.text.toString(),
                        mViewDataBinding.edtPassword.text.toString()
                    )
                }
                alarm.setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
                alarm.show()
            }
        }

        if (it.isSuccess()) {
            Logger.debug(it)
            mViewDataBinding.view?.findNavController()?.navigate(R.id.action_loginFragment_to_homeFragment)
        }
    }

    fun register(v: View) {
        v.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }

    fun forgotPassword(v: View) {
        v.findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
    }
}

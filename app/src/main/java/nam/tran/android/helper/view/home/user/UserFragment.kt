package nam.tran.android.helper.view.home.user;

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import nam.tran.android.helper.R
import nam.tran.android.helper.databinding.FragmentUserBinding
import nam.tran.android.helper.model.UserModel
import nam.tran.android.helper.view.home.user.viewmodel.IUserViewModel
import nam.tran.android.helper.view.home.user.viewmodel.UserViewModel
import nam.tran.domain.entity.state.Resource
import tran.nam.core.view.mvvm.BaseFragmentMVVM


class UserFragment : BaseFragmentMVVM<FragmentUserBinding, UserViewModel>(), IUserViewModel,
    Observer<Resource<UserModel>> {

    override fun initViewModel(factory: ViewModelProvider.Factory?) {
        mViewModel = ViewModelProviders.of(this, factory).get(UserViewModel::class.java)
    }

    override fun layoutId(): Int {
        return R.layout.fragment_user
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding?.viewModel = mViewModel
        mViewDataBinding?.view = this
        mViewModel?.user?.let {
            mViewDataBinding?.user = it
        }

        mViewModel?.results?.observe(this, this)
    }

    override fun onChanged(it: Resource<UserModel>?) {
        if (mViewModel?.type == UserViewModel.TYPE.INFO) {
            it?.data?.let {
                mViewModel?.user = it
                mViewDataBinding?.user = mViewModel?.user
            }
        } else {
            if (it != null && it.isSuccess()) {
                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
            }
        }
        mViewDataBinding?.viewModel = mViewModel
    }


    fun logout() {
        mViewModel?.logout()
        Navigation.findNavController(requireActivity().findViewById<View>(R.id.nav_host_fragment))
            .navigate(R.id.action_homeFragment_to_loginFragment)
    }

    fun picImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE) {
            val selectedImage = data?.data
            mViewModel?.user?.uri = selectedImage
            mViewDataBinding?.user = mViewModel?.user
        }
    }

    companion object {
        const val PICK_IMAGE = 123
    }
}

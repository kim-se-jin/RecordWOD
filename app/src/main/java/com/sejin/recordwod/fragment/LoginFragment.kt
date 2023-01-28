package com.sejin.recordwod.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.sejin.recordwod.R
import com.sejin.recordwod.base.BaseFragment
import com.sejin.recordwod.databinding.FragmentLoginBinding
import com.sejin.recordwod.view.state.LoginState
import com.sejin.recordwod.view.viewmodel.LoginViewModel

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun initView() {
        with(binding) {
            KakaoLoginBtn.setOnClickListener {
                Navigation.findNavController(binding.root).navigate(R.id.action_loginFragment_to_mainFragment)
            }
        }
    }

    private fun navigateToNotesScreen() {
        findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(inflater, container, false)
}
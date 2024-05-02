package com.wonderfulsigma.flick.feature.start.screen

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.sigma.data.network.dto.dauth.DauthLoginRequest
import com.sigma.data.network.dto.dauth.DauthRequest
import com.wonderfulsigma.flick.R
import com.wonderfulsigma.flick.base.BaseFragment
import com.wonderfulsigma.flick.databinding.FragmentLoginBinding
import com.wonderfulsigma.flick.feature.start.StartViewModel
import com.wonderfulsigma.flick.utils.setPopBackStack
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.security.MessageDigest

@AndroidEntryPoint
class LoginFragment: BaseFragment<FragmentLoginBinding, StartViewModel>(R.layout.fragment_login) {

    override val viewModel: StartViewModel by viewModels()

    override fun start() {
//        findNavController().navigate(LoginFragmentDirections.toHomeFragment())

        binding.toolbar.setPopBackStack()

        var id: String
        var pw: String

        binding.btnLogin.setOnClickListener {
            id = binding.etId.text.toString().trim()
            pw = binding.etPwd.text.toString().trim()

            if (id.isEmpty() || pw.isEmpty()) {
                Toast.makeText(requireContext(), "빈칸을 채워주세요", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.dauthLogin(
                    DauthLoginRequest(id, pw)
                )
            }
        }

        lifecycleScope.launch {
            viewModel.dauthLoginState.collect {
                if (it.isSuccess.isNotEmpty()) {
                    Toast.makeText(requireContext(), "로그인되었어요, 잠시만 기다려주세요", Toast.LENGTH_SHORT).show()
                    viewModel.login(DauthRequest(it.isSuccess))
                }
                if (it.error.isNotEmpty()) {
                    Toast.makeText(requireContext(), "아이디나 비밀번호를 다시 확인해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }
        lifecycleScope.launch {
            viewModel.loginState.collect {
                if (it.isSuccess) {
                    findNavController().navigate(LoginFragmentDirections.toHomeFragment())
                }
                if (it.error.isNotEmpty()) {
                    loginStateHandling(it.error)
                }
            }
        }
    }

    private fun String.toHash(): String {
        val md = MessageDigest.getInstance("SHA-512")
        md.update(this.toByteArray())
        return String.format("%0128x", BigInteger(1, md.digest()))
    }

    private fun loginStateHandling(errorMessage: String) {
        when(errorMessage) {
            "HTTP 500 " -> {
                Toast.makeText(requireContext(), "서버 에러가 일어났어요", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

}



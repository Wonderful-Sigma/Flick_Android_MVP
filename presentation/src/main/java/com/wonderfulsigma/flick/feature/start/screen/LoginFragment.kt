package com.wonderfulsigma.flick.feature.start.screen

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.sigma.data.network.dto.dauth.DauthLoginRequest
import com.wonderfulsigma.flick.R
import com.wonderfulsigma.flick.base.BaseFragment
import com.wonderfulsigma.flick.databinding.FragmentLoginBinding
import com.wonderfulsigma.flick.feature.start.StartViewModel
import com.wonderfulsigma.flick.utils.setPopBackStack
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.security.MessageDigest

class LoginFragment: BaseFragment<FragmentLoginBinding, StartViewModel>(R.layout.fragment_login) {

    override val viewModel: StartViewModel by viewModels()

    override fun start() {
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
                    DauthLoginRequest(id, pw.toHash())
                )
            }
        }

        lifecycleScope.launch {
            viewModel.dauthLoginState.collect {
                if (it.isSuccess) {
                    Toast.makeText(requireContext(), "로그인되었어요, 잠시만 기다려주세요", Toast.LENGTH_SHORT).show()
//                    viewModel.
                }
                if (it.error.isNotEmpty()) {
                    Toast.makeText(requireContext(), "아이디나 비밀번호를 다시 확인해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun String.toHash(): String {
        val md = MessageDigest.getInstance("SHA-512")
        md.update(this.toByteArray())
        return String.format("%0128x", BigInteger(1, md.digest()))
    }

}
package com.wonderfulsigma.flick.feature.setting

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wonderfulsigma.flick.R
import com.wonderfulsigma.flick.base.BaseFragment
import com.wonderfulsigma.flick.databinding.FragmentSettingBinding
import com.wonderfulsigma.flick.feature.user.viewmodel.UserViewModel
import com.wonderfulsigma.flick.utils.HiltApplication
import com.wonderfulsigma.flick.utils.setStatusBarColorWhite


class SettingFragment: BaseFragment<FragmentSettingBinding, SettingViewModel>(R.layout.fragment_setting) {

    override val viewModel: SettingViewModel by viewModels()
    private val userViewModel: UserViewModel by activityViewModels()

    override fun start() {
        setStatusBarColorWhite(requireActivity(), requireContext())

        val i = Intent(Intent.ACTION_VIEW)

        val ruleUrl = "https://sites.google.com/dgsw.hs.kr/privacy-policy/%ED%99%88"
        val updateUrl = "https://play.google.com/store/apps/details?id=com.wonderfulsigma.flick"

        userViewModel.myInfo.observe(this) { myInfo ->
            binding.tvMyName.text = myInfo?.name
            binding.tvMyAccountNumber.text = "대소코인 " + myInfo?.account!![0].number
        }

        with(binding) {
            btnBackArrow.setOnClickListener { findNavController().popBackStack() }
            btnRule.setOnClickListener {
                i.data = Uri.parse(ruleUrl)
                startActivity(i)
            }
            btnVersion.setOnClickListener {
                i.data = Uri.parse(updateUrl)
                startActivity(i)
            }
            btnLogout.setOnClickListener {
                Toast.makeText(requireContext(), "로그아웃 되었어요", Toast.LENGTH_SHORT).show()
                HiltApplication.prefs.deleteToken()
                findNavController().navigate(SettingFragmentDirections.toStartFragment())
            }
        }
    }

}
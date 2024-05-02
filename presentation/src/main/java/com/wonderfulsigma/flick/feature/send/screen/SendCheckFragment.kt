package com.wonderfulsigma.flick.feature.send.screen

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.wonderfulsigma.flick.R
import com.wonderfulsigma.flick.base.BaseFragment
import com.wonderfulsigma.flick.databinding.FragmentSendCheckBinding
import com.wonderfulsigma.flick.feature.send.viewmodel.SendViewModel
import com.wonderfulsigma.flick.feature.user.viewmodel.UserViewModel
import com.wonderfulsigma.flick.main.toDecimalFormat
//import com.wonderfulsigma.flick.utils.setDeleteBottomNav
import com.wonderfulsigma.flick.utils.setPopBackStack
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SendCheckFragment : BaseFragment<FragmentSendCheckBinding, SendViewModel>(R.layout.fragment_send_check) {

    override val viewModel: SendViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()

    override fun start() {
//        setDeleteBottomNav(activity)
        binding.toolbar.setPopBackStack()

        val myLeftCoin = userViewModel.myInfo.value!!.account[0].money
        binding.tvMyLeftPoint.text = "잔액 " + myLeftCoin.toDecimalFormat()

        val sendMoney = viewModel.sendCoin.value.toString()

        binding.tvToSendAccountName.text = viewModel.depositAccountName.value.toString()
        binding.tvToSendAccountNumber.text = viewModel.depositAccountNumber.value.toString()
        binding.tvSendCoin.text = sendMoney.toDecimalFormat()+"코인"

        binding.btnDecide.setOnClickListener {
            val action = SendCheckFragmentDirections.actionSendCheckFragmentToSendLoadFragment()
            findNavController().navigate(action)
        }
    }
}
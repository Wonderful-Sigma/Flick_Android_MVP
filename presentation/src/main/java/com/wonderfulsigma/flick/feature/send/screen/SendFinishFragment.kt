package com.wonderfulsigma.flick.feature.send.screen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.sigma.data.network.dto.account.MessageBodyRequest
import com.sigma.data.network.dto.account.RemitRequest
import com.wonderfulsigma.flick.R
import com.wonderfulsigma.flick.base.BaseFragment
import com.wonderfulsigma.flick.databinding.FragmentSendFinishBinding
import com.wonderfulsigma.flick.feature.send.viewmodel.SendViewModel
import com.wonderfulsigma.flick.feature.user.viewmodel.UserViewModel
import com.wonderfulsigma.flick.main.toDecimalFormat
import com.wonderfulsigma.flick.utils.fadeIn
//import com.wonderfulsigma.flick.utils.setDeleteBottomNav
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SendFinishFragment :
    BaseFragment<FragmentSendFinishBinding, SendViewModel>(R.layout.fragment_send_finish) {

    override val viewModel: SendViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()

    private lateinit var context: Context

    override fun start() {
        Log.d("SendFinishFragment", "start")
//        setDeleteBottomNav(activity)
        context = requireContext()

        val accountName = viewModel.depositAccountName.value
        val sendCoin = viewModel.sendCoin.value.toString()

        val remittanceAccount = userViewModel.myInfo.value!!.account[0].id
        val depositAccount = viewModel.depositAccountId.value!!
        val sendMoney = viewModel.sendCoin.value!!.toLong()

        val tvFinishTitle = binding.tvFinishTitle
        tvFinishTitle.text = "${accountName}님에게\n${sendCoin.toDecimalFormat()}코인을 보냈어요"

        lifecycleScope.launch{
            viewModel.remit(RemitRequest(remittanceAccount, sendMoney, depositAccount)).join()
            userViewModel.getUserInfo()
        }

        lifecycleScope.launch {
            viewModel.sendState.collect {
                Log.d("송금", "송금")
                if (it.isSuccess) {
                    val accountId = viewModel.depositAccountId.value
                    viewModel.postAlarm(
                        accountId.toString(),
                        MessageBodyRequest(
                            "코인을 받았습니다",
                            userViewModel.myInfo.value!!.name + "에게 ${sendCoin.toDecimalFormat()}코인을 받았습니다"
                        )
                    )
                }
                if (it.error.isNotEmpty()) {
                    binding.animationCompletion.setAnimation(R.raw.animation_warning)
                    tvFinishTitle.text = "${accountName}님에게\n${sendCoin.toDecimalFormat()}코인 보내기를\n실패했어요"
                    Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnComplete.fadeIn(context)

        binding.btnComplete.setOnClickListener {
            findNavController().navigate(R.id.to_homeFragment)
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    /** 뒤로가기 막기 */
                }
            })
    }
}
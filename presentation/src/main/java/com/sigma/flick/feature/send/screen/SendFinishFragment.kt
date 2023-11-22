package com.sigma.flick.feature.send.screen

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.sigma.data.network.dto.account.MessageBodyRequest
import com.sigma.flick.R
import com.sigma.flick.base.BaseFragment
import com.sigma.flick.databinding.FragmentSendFinishBinding
import com.sigma.flick.feature.send.viewmodel.SendViewModel
import com.sigma.flick.utils.fadeIn
import com.sigma.flick.utils.setDeleteBottomNav
import com.sigma.flick.utils.slideUpAndFadeIn
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class SendFinishFragment : BaseFragment<FragmentSendFinishBinding, SendViewModel>(R.layout.fragment_send_finish) {

    override val viewModel: SendViewModel by activityViewModels()

    private lateinit var context: Context

    override fun start() {
        setDeleteBottomNav(activity)

        context = requireContext()

        val accountName = viewModel.depositAccountName.value
        val sendCoin = viewModel.sendCoin.value.toString()

        val tvFinishTitle = binding.tvFinishTitle
        tvFinishTitle.text = "${accountName}님에게\n${sendCoin}코인을 보낼게요"
        tvFinishTitle.slideUpAndFadeIn(context)


        runBlocking {
            lifecycleScope.launch {
                delay(1000)
            }

            lifecycleScope.launch {
                viewModel.sendState.collect {
                    if (it.isSuccess) {
                        val accountId = viewModel.depositAccountId.value
                        viewModel.postAlarm(accountId.toString(), MessageBodyRequest("title","body"))
                        tvFinishTitle.text = "${accountName}님에게\n${sendCoin}코인을 보냈어요"
                    }
                    if (it.error.isNotEmpty()) {
                        binding.animationCompletion.setAnimation(R.raw.animation_warning)
                        tvFinishTitle.text = "${accountName}님에게\n${sendCoin}코인 보내기를\n실패했어요"
                        Toast.makeText(context, it.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }




        binding.btnComplete.fadeIn(context)

        binding.btnComplete.setOnClickListener {
            findNavController().navigate(R.id.to_homeFragment)
        }
    }
}
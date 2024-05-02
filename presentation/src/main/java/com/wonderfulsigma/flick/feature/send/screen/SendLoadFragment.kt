package com.wonderfulsigma.flick.feature.send.screen

import android.os.Handler
import android.util.Log
import android.window.OnBackInvokedCallback
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.wonderfulsigma.flick.R
import com.wonderfulsigma.flick.base.BaseFragment
import com.wonderfulsigma.flick.databinding.FragmentSendLoadBinding
import com.wonderfulsigma.flick.feature.send.viewmodel.SendViewModel
import com.wonderfulsigma.flick.main.toDecimalFormat
//import com.wonderfulsigma.flick.utils.setDeleteBottomNav

class SendLoadFragment :
    BaseFragment<FragmentSendLoadBinding, SendViewModel>(R.layout.fragment_send_load) {

    override val viewModel: SendViewModel by activityViewModels()

    override fun start() {
//        setDeleteBottomNav(activity)

        val accountName = viewModel.depositAccountName.value
        val sendCoin = viewModel.sendCoin.value.toString()

        binding.tvLoadTitle.text = "${accountName}님에게\n${sendCoin.toDecimalFormat()}코인을 보낼게요"

        Handler().postDelayed({
            Log.d("다음","다음")
            val action = SendLoadFragmentDirections.toSendFinishFragment()
            findNavController().navigate(action)
        }, 1500L)

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    /** 뒤로가기 막기 */
                }
            })
    }


}
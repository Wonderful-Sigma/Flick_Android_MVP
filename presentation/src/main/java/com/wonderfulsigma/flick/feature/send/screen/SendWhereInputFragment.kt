package com.wonderfulsigma.flick.feature.send.screen

import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.wonderfulsigma.flick.R
import com.wonderfulsigma.flick.base.BaseFragment
import com.wonderfulsigma.flick.databinding.FragmentSendWhereInputBinding
import com.wonderfulsigma.flick.feature.send.state.AccountNumberState
import com.wonderfulsigma.flick.feature.send.viewmodel.SendViewModel
//import com.wonderfulsigma.flick.utils.setDeleteBottomNav
import com.wonderfulsigma.flick.utils.setPopBackStack
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SendWhereInputFragment : BaseFragment<FragmentSendWhereInputBinding, SendViewModel>(R.layout.fragment_send_where_input) {

    override val viewModel: SendViewModel by activityViewModels()

    private lateinit var etNumber: String

    override fun start() {
//        setDeleteBottomNav(activity)
        binding.toolbar.setPopBackStack()

        val context = requireContext() //5637-4925-5126-8

        binding.btnOk.setOnClickListener {
            etNumber = binding.etInputAccount.text.toString()
            if (etNumber.isNotEmpty()) {
                viewModel.checkAccount(etNumber)
            } else {
                Toast.makeText(requireContext(), "빈칸을 채워주세요", Toast.LENGTH_SHORT).show()
            }
        }

        lifecycleScope.launch {
            viewModel.accountCheckState.collect {
                if (it.isSuccess) {
                    viewModel.setDepositAccountNumber(etNumber)
                    findNavController().navigate(SendWhereInputFragmentDirections.toSendPointFragment())
                }
                if (it.error.isNotEmpty()) {
                    Toast.makeText(context, "계좌번호를 찾지 못했어요", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
package com.sigma.flick.feature.tabs.home

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.messaging.FirebaseMessaging
import com.sigma.flick.R
import com.sigma.flick.base.BaseFragment
import com.sigma.flick.databinding.FragmentHomeBinding
import com.sigma.flick.feature.tabs.home.viewmodel.HomeViewModel
import com.sigma.flick.main.MainActivity
import com.sigma.flick.feature.user.viewmodel.UserViewModel
import com.sigma.flick.feature.qrcode.QRCode
import com.sigma.flick.utils.setStatusBarColorBackground
import com.sigma.main.model.account.Account
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

@AndroidEntryPoint
class HomeFragment: BaseFragment<FragmentHomeBinding, HomeViewModel>(R.layout.fragment_home) {

    override val viewModel: HomeViewModel by viewModels()
    val userViewModel: UserViewModel by activityViewModels()

    private lateinit var context: Context

    override fun start() {
        context = requireContext()
        setStatusBarColorBackground(requireActivity(), context)

        observeMyInfo()

        /** QR Code */
        showQRCode(context)

        //** navigation */
        setNavigation()
    }

    private fun observeMyInfo() {
        userViewModel.myInfo.observe(viewLifecycleOwner) { myInfo ->
            val myAccount = myInfo?.account!![0]
            binding.tvMyAccount.text = myAccount.name
            binding.tvMyCoin.text = getDecimalFormat(myAccount.money)
        }
    }

    private fun setNavigation() {
        with(binding){
            btnSend.setOnClickListener { findNavController().navigate(R.id.to_sendWhereFragment) }
            linearMyAccount.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_fragmentBankbookRecords) }
            bankbookButton.setOnClickListener { findNavController().navigate(R.id.to_accountDetailFragment) }
            alarmButton.setOnClickListener {
                Toast.makeText(context, "아직은 알림 기능이 없어요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showQRCode(context: Context) {
        val qrCodeClass = QRCode(userViewModel, context, viewLifecycleOwner, this@HomeFragment, layoutInflater)

        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(qrCodeClass.bottomSheetView)

        binding.paymentButton.setOnClickListener {
            bottomSheetDialog.show()
            qrCodeClass.generateQRCode()
            qrCodeClass.observeMyCoin() // todo : 계속 null 코인이 뜸
        }
        qrCodeClass.observeQRCode(viewLifecycleOwner)

    }

    private fun getDecimalFormat(number: Long): String {
        val decimalFormat = DecimalFormat("#,###")
        return decimalFormat.format(number)+"코인"
    }

    companion object {
        const val TAG = "HomeFragment"
    }

}
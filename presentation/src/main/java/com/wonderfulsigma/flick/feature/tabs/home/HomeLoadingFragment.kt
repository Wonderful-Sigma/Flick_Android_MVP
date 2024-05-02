package com.wonderfulsigma.flick.feature.tabs.home

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sigma.data.network.dto.account.Account
import com.wonderfulsigma.flick.R
import com.wonderfulsigma.flick.base.BaseFragment
import com.wonderfulsigma.flick.databinding.FragmentHomeBinding
import com.wonderfulsigma.flick.databinding.FragmentHomeLoadingBinding
import com.wonderfulsigma.flick.feature.qrcode.QRCode
import com.wonderfulsigma.flick.feature.tabs.home.viewmodel.HomeViewModel
import com.wonderfulsigma.flick.feature.user.viewmodel.UserViewModel
import com.wonderfulsigma.flick.main.toDecimalFormat
import com.wonderfulsigma.flick.utils.setStatusBarColorBackground
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeLoadingFragment : BaseFragment<FragmentHomeLoadingBinding, HomeViewModel>(R.layout.fragment_home_loading) {

    override val viewModel: HomeViewModel by viewModels()

    override fun start() {
//        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
//        transaction.replace(R.id.fragment_container, this)
//        transaction.commit()
    }

}
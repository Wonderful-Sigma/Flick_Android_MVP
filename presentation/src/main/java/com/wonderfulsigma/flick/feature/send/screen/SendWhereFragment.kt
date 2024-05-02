package com.wonderfulsigma.flick.feature.send.screen

//import com.wonderfulsigma.flick.utils.setDeleteBottomNav

import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.wonderfulsigma.flick.R
import com.wonderfulsigma.flick.base.BaseFragment
import com.wonderfulsigma.flick.databinding.FragmentSendWhereBinding
import com.wonderfulsigma.flick.feature.send.recyclerview.recent.RecentAccount
import com.wonderfulsigma.flick.feature.send.recyclerview.recent.RecentAccountAdapter
import com.wonderfulsigma.flick.feature.send.viewmodel.SendViewModel
import com.wonderfulsigma.flick.feature.user.viewmodel.UserViewModel
import com.wonderfulsigma.flick.utils.setPopBackStack
import com.wonderfulsigma.flick.utils.setStatusBarColorWhite
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SendWhereFragment : BaseFragment<FragmentSendWhereBinding, SendViewModel>(R.layout.fragment_send_where), RecentAccountAdapter.OnRecentAccountItemClickListener {

    override val viewModel: SendViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()

    private lateinit var context: Context

    private var recentAccountList: MutableList<RecentAccount> = mutableListOf()

    private lateinit var etNumber: String

    override fun start() {
//        setDeleteBottomNav(activity)
        binding.toolbar.setPopBackStack()
        setStatusBarColorWhite(requireActivity(), requireContext())

        context = requireContext()

        /** Recent Spend List */

        getRecentAccount()

        with(binding){
//            etInputAccount.setOnClickListener { findNavController().navigate(R.id.action_sendWhereFragment_to_sendWhereInputFragment) }
            toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        }

        handlingKeyboardOk()

        lifecycleScope.launch {
            viewModel.accountCheckState.collect {
                if (it.isSuccess) {
                    viewModel.setDepositAccountNumber(etNumber)
                    findNavController().navigate(SendWhereFragmentDirections.toSendPointFragment())
                }
                if (it.error.isNotEmpty()) {
                    Toast.makeText(context, "계좌번호를 찾지 못했어요", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun handlingKeyboardOk() {
        binding.etInputAccount.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                etNumber = binding.etInputAccount.text.toString()
                if (etNumber.isNotEmpty()) {
                    viewModel.checkAccount(etNumber)
                } else {
                    Toast.makeText(requireContext(), "빈칸을 채워주세요", Toast.LENGTH_SHORT).show()
                }
            }
            false
        }
    }

    private fun setRecentAccountList() {
        val recentAccountRecyclerView = binding.recyclerviewRecentAccount
        recentAccountRecyclerView.layoutManager = LinearLayoutManager(context)

        val recentAccountAdapter = RecentAccountAdapter(recentAccountList, this)
        recentAccountRecyclerView.adapter = recentAccountAdapter
    }

    override fun onRecentAccountItemClick(position: Int) {
        val clickAccount = recentAccountList[position]
        viewModel.setDepositAccountNumber(clickAccount.accountNumber)

        val action = SendWhereFragmentDirections.toSendPointFragment()
        findNavController().navigate(action)
    }

    private fun getRecentAccount() {
        viewModel.getRecentSpendList(userViewModel.myInfo.value!!.id)

        viewModel.recentSpendList.observe(viewLifecycleOwner) {
            recentAccountList = mutableListOf()

            viewModel.recentSpendList.value?.forEach { account ->
                Log.d(TAG, "start: List Add!!")
                recentAccountList.add(
                    RecentAccount(
                        account.name,
                        account.number,
                        R.drawable.ic_star
                    )
                )
            }
            setRecentAccountList()

            if (recentAccountList.isEmpty()) {
                binding.tvRecentAccount.visibility = View.GONE
            } else {
                binding.tvNothingRecentAccount.visibility = View.GONE
            }
        }

    }

    companion object {
        const val TAG = "SendWhereFragment"
    }
}
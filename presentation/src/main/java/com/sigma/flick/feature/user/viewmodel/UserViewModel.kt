package com.sigma.flick.feature.user.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sigma.data.network.dto.account.AccountObject
import com.sigma.data.network.dto.user.UserResponse
import com.sigma.data.repository.AccountRepository
import com.sigma.data.repository.MemberRepository
import com.sigma.data.repository.QRCodeRepository
import com.sigma.flick.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val qrCodeRepository: QRCodeRepository,
    private val accountRepository: AccountRepository
) : BaseViewModel() {

    private var _myInfo = MutableLiveData<UserResponse>()
    val myInfo: LiveData<UserResponse> = _myInfo
    private var _accountData = MutableLiveData<AccountObject>()
    val accountData: LiveData<AccountObject> = _accountData

    private var _jwt = MutableLiveData<String>()
    val jwt: LiveData<String> = _jwt

//    var collectionData: MutableList<ItemGroupBankBookData> = mutableListOf()

    fun getUserInfo() = viewModelScope.launch {
        kotlin.runCatching {
            memberRepository.getUser()
        }.onSuccess {
            Log.d(TAG, "getUser Success!!: $it")
            _myInfo.value = it
        }.onFailure { e ->
            Log.d(TAG, "getUser Failed..:  $e")
        }
    }
    
     fun generateJwt(walletId: Long) = viewModelScope.launch {
        kotlin.runCatching {
            qrCodeRepository.generateJwt(walletId)
        }.onSuccess {
            Log.d(TAG, "generateJwt: success!! $it")
            _jwt.value = it.qrJwt
        }.onFailure { e ->
            Log.d(TAG, "generateJwt: failed.. $e")
        }
    }
    
    fun getAccount(accountNumber: String) = viewModelScope.launch {
        kotlin.runCatching {
            accountRepository.getAccount(accountNumber)
        }.onSuccess {
            Log.d(TAG, "getAccount Success!!: $it")
            _accountData.value = it
        }.onFailure { e ->
            Log.d(TAG, "getAccount Failed..:  $e")
        }
    }

    companion object {
        const val TAG = "UserViewModel"
    }
}
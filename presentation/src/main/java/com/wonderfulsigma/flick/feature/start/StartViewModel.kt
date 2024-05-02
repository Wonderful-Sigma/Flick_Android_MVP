package com.wonderfulsigma.flick.feature.start

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.b1nd.dauth.DAuth
import com.sigma.data.network.api.DauthApi
import com.sigma.data.network.api.UserApi
import com.sigma.data.network.dto.dauth.DauthLoginRequest
import com.sigma.data.network.dto.dauth.DauthRequest
import com.wonderfulsigma.flick.base.BaseViewModel
import com.wonderfulsigma.flick.feature.start.state.DauthLoginState
import com.wonderfulsigma.flick.feature.start.state.LoginState
import com.wonderfulsigma.flick.utils.HiltApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class StartViewModel @Inject constructor(
    private val dauthApi: DauthApi,
    private val userApi: UserApi,
    private val dAuth: DAuth
) : BaseViewModel() {

    private var _autoLogin = MutableLiveData(HiltApplication.prefs.autoLogin)
    val autoLogin: LiveData<Boolean> = _autoLogin

    private var _dauthLoginState = MutableSharedFlow<DauthLoginState>()
    val dauthLoginState: SharedFlow<DauthLoginState> = _dauthLoginState

    private var _loginState = MutableSharedFlow<LoginState>()
    val loginState: SharedFlow<LoginState> = _loginState

    fun dauthLogin(dauthLoginRequest: DauthLoginRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val code = dAuth.issueCode(dauthLoginRequest.id, dauthLoginRequest.pw).extractCode()
                _dauthLoginState.emit(DauthLoginState(isSuccess = code))
            } catch (e: Exception) {
                _dauthLoginState.emit(DauthLoginState(error = "$e"))
            }
        }
    }

    fun login(dauthRequestDto: DauthRequest) = viewModelScope.launch {
        kotlin.runCatching {
            userApi.login(dauthRequestDto)
        }.onSuccess {
            Log.d(TAG, "LoginSuccess! $it")
            _loginState.emit(LoginState(isSuccess = true))
            HiltApplication.prefs.autoLogin = true
            _autoLogin.value = true
            HiltApplication.prefs.accessToken = it.accessToken
            HiltApplication.prefs.refreshToken = it.refreshToken
        }.onFailure { e ->
            Log.d(TAG, "LoginFailed.. $e")
            _loginState.emit(LoginState(error = "$e"))
        }
    }

    companion object {
        private const val TAG = "StartViewModel"
    }
}
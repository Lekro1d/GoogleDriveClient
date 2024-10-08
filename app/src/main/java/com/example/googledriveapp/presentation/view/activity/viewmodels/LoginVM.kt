package com.example.googledriveapp.presentation.view.activity.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googledriveapp.domain.usecase.googleAccount.SignInUseCase
import com.example.googledriveapp.model.googleAccount.LoginState
import kotlinx.coroutines.launch

class LoginVM(
    private val signInUseCase: SignInUseCase
): ViewModel() {
    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> get() = _loginState

    fun signIn(idToken: String) {
        viewModelScope.launch {
            val result = signInUseCase.signIn(idToken)
            _loginState.value = result
        }
    }
}
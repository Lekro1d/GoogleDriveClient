package com.example.googledriveapp.presentation.view.activity.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.googledriveapp.domain.usecase.googleAccount.SignInUseCase

class LoginViewModelFactory (
    private val signInUseCase: SignInUseCase
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginVM(signInUseCase) as T
    }
}
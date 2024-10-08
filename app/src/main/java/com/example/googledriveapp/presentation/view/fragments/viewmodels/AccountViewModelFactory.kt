package com.example.googledriveapp.presentation.view.fragments.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.googledriveapp.domain.usecase.googleAccount.LastSignedInUseCase
import com.example.googledriveapp.domain.usecase.googleAccount.SignOutUseCase
import com.google.android.gms.auth.api.signin.GoogleSignInClient

class AccountViewModelFactory(
    private val signOut: SignOutUseCase,
    private val getLastSignedIn: LastSignedInUseCase
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AccountVM (
            signOut,
            getLastSignedIn
        ) as T
    }
}
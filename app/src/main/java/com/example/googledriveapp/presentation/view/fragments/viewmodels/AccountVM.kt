package com.example.googledriveapp.presentation.view.fragments.viewmodels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.googledriveapp.domain.usecase.googleAccount.LastSignedInUseCase
import com.example.googledriveapp.domain.usecase.googleAccount.SignOutUseCase
import com.google.android.gms.auth.api.signin.GoogleSignInClient

class AccountVM(
    private val signOut: SignOutUseCase,
    private val getLastSignedIn: LastSignedInUseCase
): ViewModel() {

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    private val _userEmail = MutableLiveData<String>()
    val userEmail: LiveData<String> get() = _userEmail

    private val _userPhotoUrl = MutableLiveData<Uri>()
    val userPhotoUrl: LiveData<Uri> get() = _userPhotoUrl

    fun initGoogleSignIn() {
        getLastSignedIn.getLastSignedInAccount()?.let { googleAccount ->
            _userName.value = googleAccount.displayName
            _userEmail.value = googleAccount.email
            _userPhotoUrl.value = googleAccount.photoUrl
        }
    }

    fun signOut(onSignOutComplete: () -> Unit) {
        signOut.signOut(onSignOutComplete)
    }
}
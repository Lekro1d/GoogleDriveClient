package com.example.googledriveapp.domain.repository

import com.example.googledriveapp.model.googleAccount.LoginState
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

interface AccountRepository {
    suspend fun signIn(idToken: String): LoginState
    fun signOut(onSignOutComplete: () -> Unit)
    fun getLastSignedAccount(): GoogleSignInAccount?
}
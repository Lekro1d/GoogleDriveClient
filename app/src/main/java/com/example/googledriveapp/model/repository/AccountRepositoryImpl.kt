package com.example.googledriveapp.model.repository

import com.example.googledriveapp.domain.repository.AccountRepository
import com.example.googledriveapp.model.googleAccount.LastSignedInAccount
import com.example.googledriveapp.model.googleAccount.LoginState
import com.example.googledriveapp.model.googleAccount.SignInAccount
import com.example.googledriveapp.model.googleAccount.SignOutAccount
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class AccountRepositoryImpl(
    private val signInAccount: SignInAccount,
    private val signOutAccount: SignOutAccount,
    private val lastSignedInAccount: LastSignedInAccount
): AccountRepository {

    override suspend fun signIn(idToken: String): LoginState {
        return signInAccount.signIn(idToken)
    }

    override fun signOut(onSignOutComplete: () -> Unit) {
        return signOutAccount.signOut(onSignOutComplete)
    }

    override fun getLastSignedAccount(): GoogleSignInAccount? {
        return lastSignedInAccount.getLastSignedInAccount()
    }
}
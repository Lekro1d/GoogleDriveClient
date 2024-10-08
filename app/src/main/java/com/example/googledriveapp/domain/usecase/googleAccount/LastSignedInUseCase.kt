package com.example.googledriveapp.domain.usecase.googleAccount

import android.content.Context
import com.example.googledriveapp.domain.repository.AccountRepository
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import javax.inject.Inject

class LastSignedInUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    fun getLastSignedInAccount(): GoogleSignInAccount? {
        return accountRepository.getLastSignedAccount()
    }
}
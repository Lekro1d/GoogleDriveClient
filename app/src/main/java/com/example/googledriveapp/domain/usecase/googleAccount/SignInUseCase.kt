package com.example.googledriveapp.domain.usecase.googleAccount

import com.example.googledriveapp.domain.repository.AccountRepository
import com.example.googledriveapp.model.googleAccount.LoginState
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend fun signIn(idToken: String) : LoginState {
        return accountRepository.signIn(idToken)
    }
}
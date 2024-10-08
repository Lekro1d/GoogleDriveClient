package com.example.googledriveapp.domain.usecase.googleAccount

import com.example.googledriveapp.domain.repository.AccountRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    fun signOut(onSignOutComplete: () -> Unit) {
        accountRepository.signOut(onSignOutComplete)
    }
}
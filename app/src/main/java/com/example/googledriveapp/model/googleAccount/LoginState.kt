package com.example.googledriveapp.model.googleAccount

sealed class LoginState {
    data class SuccessSignIn(private val result: Result<Unit?>): LoginState()
    data class ErrorSignIn(private val result: Result<Unit?>): LoginState()
}
package com.example.googledriveapp.model.googleAccount

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SignInAccount @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) {
    suspend fun signIn(idToken: String): LoginState {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            firebaseAuth.signInWithCredential(credential).await()
            LoginState.SuccessSignIn(Result.success(Unit))
        } catch (e: Exception) {
            LoginState.ErrorSignIn(Result.failure(e))
        }
    }
}
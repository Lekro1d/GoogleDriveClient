package com.example.googledriveapp.model.googleAccount

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class SignOutAccount @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val googleSignInClient: GoogleSignInClient
) {
    fun signOut(onSignOutComplete: () -> Unit) {
        firebaseAuth.signOut()
        googleSignInClient.signOut().addOnCompleteListener {
            onSignOutComplete()
        }
    }
}
package com.example.googledriveapp.model.googleAccount

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import javax.inject.Inject

class LastSignedInAccount @Inject constructor(
    private val context: Context
) {
    fun getLastSignedInAccount(): GoogleSignInAccount? {
        return GoogleSignIn.getLastSignedInAccount(context)
    }
}
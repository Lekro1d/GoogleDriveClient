package com.example.googledriveapp.vmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.DriveScopes
import com.google.firebase.auth.FirebaseAuth

class AccountViewModel: ViewModel() {
    lateinit var mGoogleSignInClient: GoogleSignInClient

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    private val _userEmail = MutableLiveData<String>()
    val userEmail: LiveData<String> get() = _userEmail

    private val _userPhotoUrl = MutableLiveData<Uri>()
    val userPhotoUrl: LiveData<Uri> get() = _userPhotoUrl

    fun initializeGoogleSignInClient(context: Context, clientId: String) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(clientId)
            .requestEmail()
            .requestScopes(Scope(DriveScopes.DRIVE_FILE))
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso)

        GoogleSignIn.getLastSignedInAccount(context)?.let { googleAccount ->
            _userName.value = googleAccount.displayName
            _userEmail.value = googleAccount.email
            _userPhotoUrl.value = googleAccount.photoUrl
        }
    }

    fun signOut(onSignOutComplete: () -> Unit) {
        FirebaseAuth.getInstance().signOut()
        mGoogleSignInClient.signOut().addOnCompleteListener {
            onSignOutComplete()
        }
    }
}
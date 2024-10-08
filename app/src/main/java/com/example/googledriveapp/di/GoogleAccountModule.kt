package com.example.googledriveapp.di

import android.content.Context
import com.example.googledriveapp.R
import com.example.googledriveapp.domain.repository.AccountRepository
import com.example.googledriveapp.model.googleAccount.LastSignedInAccount
import com.example.googledriveapp.model.googleAccount.SignInAccount
import com.example.googledriveapp.model.googleAccount.SignOutAccount
import com.example.googledriveapp.model.repository.AccountRepositoryImpl
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.DriveScopes
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class GoogleAccountModule {
    @Provides
    fun provideAccountRepository(
        signInAccount: SignInAccount,
        signOutAccount: SignOutAccount,
        lastSignedInAccount: LastSignedInAccount
    ): AccountRepository {
        return AccountRepositoryImpl(
            signInAccount,
            signOutAccount,
            lastSignedInAccount
        )
    }

    @Provides
    @Singleton
    fun provideGoogleSignInClient(context: Context): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .requestScopes(Scope(DriveScopes.DRIVE_FILE))
            .build()

        return GoogleSignIn.getClient(context, gso)
    }

    @Provides
    @Singleton
    fun provideFireBase(): FirebaseAuth = FirebaseAuth.getInstance()
}
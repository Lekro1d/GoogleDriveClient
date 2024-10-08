package com.example.googledriveapp.model.googledrive

import android.content.Context
import com.example.googledriveapp.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DriveService @Inject constructor() {
    private var driveService: Drive? = null

    fun getGoogleDriveService(context: Context): Drive? {
        //Получаем файлы от текущего пользователя
        GoogleSignIn.getLastSignedInAccount(context)?.let { googleAccount ->
            val credential = GoogleAccountCredential.usingOAuth2(
                context, listOf(DriveScopes.DRIVE_FILE)
            )
            credential.selectedAccount = googleAccount.account!!

            driveService = Drive
                .Builder(
                    AndroidHttp.newCompatibleTransport(),
                    JacksonFactory.getDefaultInstance(),
                    credential
                )
                .setApplicationName(context.getString(R.string.app_name))
                .build()
        }
        return driveService
    }
}
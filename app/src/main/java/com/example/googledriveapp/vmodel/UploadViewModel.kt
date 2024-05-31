package com.example.googledriveapp.vmodel

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googledriveapp.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.InputStreamContent
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UploadViewModel: ViewModel() {
    private lateinit var driveService: Drive

    fun googleDriveService(context: Context) {
        GoogleSignIn.getLastSignedInAccount(context)?.let { googleAccount ->

            // Get credentials
            val credential = GoogleAccountCredential.usingOAuth2(
                context, listOf(DriveScopes.DRIVE_FILE)
            )
            credential.selectedAccount = googleAccount.account!!

            // Get Drive Instance
            driveService = Drive
                .Builder(
                    AndroidHttp.newCompatibleTransport(),
                    JacksonFactory.getDefaultInstance(),
                    credential
                )
                .setApplicationName(context.getString(R.string.app_name))
                .build()
        }
    }
    //!!
    fun uploadImageFile(fileUri: Uri, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (!::driveService.isInitialized) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Drive service not initialized", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                val contentResolver = context.contentResolver
                val mimeType = contentResolver.getType(fileUri)
                val inputStream = contentResolver.openInputStream(fileUri)
                val fileMetadata = File().apply {
                    name = fileUri.lastPathSegment
                    this.mimeType = mimeType
                }

                val mediaContent = InputStreamContent(mimeType, inputStream)

                val driveFile = driveService.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute()

                withContext(Dispatchers.Main) {
                    if (driveFile != null) {
                        Toast.makeText(context, "File ID: ${driveFile.id}", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to upload file", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error uploading file: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
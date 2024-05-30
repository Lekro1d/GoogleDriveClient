package com.example.googledriveapp.vmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googledriveapp.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.ByteArrayContent
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MenuViewModel: ViewModel() {
    private lateinit var driveService: Drive

    private val _files = MutableLiveData<List<com.google.api.services.drive.model.File>>()
    val files: LiveData<List<com.google.api.services.drive.model.File>> get() = _files

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
    fun listFiles(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (!::driveService.isInitialized) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Drive service not initialized", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                val result = driveService.files().list()
                    .setFields("files(id, name, mimeType)")
                    .execute()

                val files = result.files ?: emptyList()

                withContext(Dispatchers.Main) {
                    _files.value = files
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error retrieving files: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
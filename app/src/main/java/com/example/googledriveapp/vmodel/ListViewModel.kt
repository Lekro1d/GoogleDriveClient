package com.example.googledriveapp.vmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.content.FileProvider
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
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream

class ListViewModel: ViewModel() {
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
                    .setFields("files(id, name, mimeType, webViewLink, thumbnailLink)")
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

    fun downloadFile(context: Context, file: com.google.api.services.drive.model.File) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val outputStream = ByteArrayOutputStream()
                driveService.files().get(file.id).executeMediaAndDownloadTo(outputStream)

                val outputFile = java.io.File(context.getExternalFilesDir(null), file.name)
                val fileOutputStream = FileOutputStream(outputFile)
                fileOutputStream.write(outputStream.toByteArray())
                fileOutputStream.close()

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "File downloaded: ${outputFile.absolutePath}", Toast.LENGTH_LONG).show()

                    // Создайть URI для загруженного файла.
                    val fileUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", outputFile)

                    // Определить тип MIME
                    val mimeType = getMimeType(file.name)

                    // Создайте путь для просмотра файла
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(fileUri, mimeType)
                        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
                    }

                    // Запуск активности, чтобы просмотреть файл
                    context.startActivity(intent)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error downloading file: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Функция получения типа MIME по имени файла
    private fun getMimeType(fileName: String): String {
        val extension = fileName.substringAfterLast('.', "")
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) ?: "application/octet-stream"
    }
}
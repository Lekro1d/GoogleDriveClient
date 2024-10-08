package com.example.googledriveapp.model.googledrive

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.google.api.client.http.InputStreamContent
import com.google.api.services.drive.model.File
import javax.inject.Inject

class DriveUploadFile @Inject constructor(
    private val context: Context,
    private val driveService: DriveService) {

    fun uploadFile(fileUri: Uri): DriveResult {
        val service = driveService.getGoogleDriveService(context)
        return try {
            val contentResolver = context.contentResolver //Доступ контента через URI
            val mimeType = contentResolver.getType(fileUri) //Определяет тип MIME-файла
            val inputStream =
                contentResolver.openInputStream(fileUri) //Открывает поток для чтения содержимого файла по его Uri

            //Получает имя файла.  Если имя не удаётся получить, используется последний сегмент пути Uri
            val fileName = getFileName(fileUri, context) ?: fileUri.lastPathSegment ?: "Unknown"

            val fileMetadata = File().apply {
                name = fileName
                this.mimeType = mimeType
            }

            val mediaContent = InputStreamContent(mimeType, inputStream) //Загружает файл на Google Диск

            val driveFile = service?.files()?.create(fileMetadata, mediaContent)
                ?.setFields("id")
                ?.execute()

            if (driveFile != null) {
                DriveResult.FileUpload("Загружен файл: ${driveFile.id}")
            } else DriveResult.FileUpload("Провал загрузки файла")
        } catch (e: Exception) {
            DriveResult.Error("Ошибка загрузки файла")
        }
    }

    private fun getFileName(uri: Uri, context: Context): String? {
        var name: String? = null
        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    name = it.getString(nameIndex)
                }
            }
        }
        return name
    }
}
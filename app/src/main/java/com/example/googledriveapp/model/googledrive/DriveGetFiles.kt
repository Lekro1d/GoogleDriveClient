package com.example.googledriveapp.model.googledrive

import android.content.Context
import javax.inject.Inject

class DriveGetFiles @Inject constructor(
    private val context: Context,
    private val driveService: DriveService) {

    fun getDriveFiles(): DriveResult {
        val service = driveService.getGoogleDriveService(context)

        return try {
                service?.let {
                    val result = it.files().list()
                        .setFields("files(id, name, mimeType, webViewLink, thumbnailLink, size, createdTime)")
                        .execute()

                    DriveResult.GetFileCollection(result.files)
                } ?: DriveResult.GetFileCollection(emptyList()) // Возвращаем пустой список, если Drive не инициализирован
            } catch (e: Exception) {
            DriveResult.Error("${e.message}")
        }
    }
}
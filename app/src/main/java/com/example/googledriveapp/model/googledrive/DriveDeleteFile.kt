package com.example.googledriveapp.model.googledrive

import android.content.Context
import com.google.api.services.drive.model.File
import javax.inject.Inject

class DriveDeleteFile @Inject constructor(
    private val context: Context,
    private val driveService: DriveService) {

    fun deleteFile(file: File): DriveResult {
        val service = driveService.getGoogleDriveService(context)

        return try {
            service?.files()?.delete(file.id)?.execute()
            DriveResult.FileDeleted("Файл удалён: ${file.name}")
        } catch (e: Exception) {
            DriveResult.Error("Ошибка удаления файла: ${e.message}")
        }
    }
}
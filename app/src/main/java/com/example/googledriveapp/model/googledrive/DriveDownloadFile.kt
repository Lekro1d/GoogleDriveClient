package com.example.googledriveapp.model.googledrive

import android.content.Context
import com.google.api.services.drive.model.File
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import javax.inject.Inject

class DriveDownloadFile @Inject constructor(
    private val context: Context,
    private val driveService: DriveService) {

    fun downloadFile(file: File): DriveResult {
        val service = driveService.getGoogleDriveService(context)

        return try {
            val outputStream = ByteArrayOutputStream()
            service?.files()?.get(file.id)?.executeMediaAndDownloadTo(outputStream)

            val outputFile = java.io.File(context.getExternalFilesDir(null), file.name)
            val fileOutputStream = FileOutputStream(outputFile)
            fileOutputStream.write(outputStream.toByteArray())
            fileOutputStream.close()

            DriveResult.FileDownloaded(outputFile)
        } catch (e: Exception) {
            DriveResult.Error("${e.message}")
        }
    }
}
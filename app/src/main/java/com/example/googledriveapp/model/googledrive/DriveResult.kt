package com.example.googledriveapp.model.googledrive

import android.net.Uri
import com.google.api.services.drive.model.File

sealed class DriveResult {
    data class FileDownloaded(val file: java.io.File): DriveResult()
    data class GetFileCollection(val files: List<File>): DriveResult()
    data class Error(val message: String): DriveResult()
    data class FileDeleted(val message: String): DriveResult()
    data class FileUpload(val message: String): DriveResult()
}
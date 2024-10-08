package com.example.googledriveapp.domain.repository

import android.net.Uri
import com.example.googledriveapp.model.googledrive.DriveResult
import com.google.api.services.drive.model.File

interface FilesRepository {
    suspend fun getFiles(): DriveResult
    suspend fun downloadFile(file: File): DriveResult
    suspend fun deleteFile(file: File): DriveResult
    suspend fun uploadFile(fileUri: Uri): DriveResult
}
package com.example.googledriveapp.model.repository

import android.net.Uri
import com.example.googledriveapp.domain.repository.FilesRepository
import com.example.googledriveapp.model.googledrive.DriveDeleteFile
import com.example.googledriveapp.model.googledrive.DriveDownloadFile
import com.example.googledriveapp.model.googledrive.DriveGetFiles
import com.example.googledriveapp.model.googledrive.DriveResult
import com.example.googledriveapp.model.googledrive.DriveUploadFile
import com.google.api.services.drive.model.File

class FilesRepositoryImpl(
    private val getFiles: DriveGetFiles,
    private val downloadFile: DriveDownloadFile,
    private val deleteFile: DriveDeleteFile,
    private val uploadFile: DriveUploadFile
): FilesRepository {

    override suspend fun getFiles(): DriveResult {
        return getFiles.getDriveFiles()
    }

    override suspend fun downloadFile(file: File): DriveResult {
        return downloadFile.downloadFile(file)
    }

    override suspend fun deleteFile(file: File): DriveResult {
        return deleteFile.deleteFile(file)
    }

    override suspend fun uploadFile(fileUri: Uri): DriveResult {
        return uploadFile.uploadFile(fileUri)
    }
}
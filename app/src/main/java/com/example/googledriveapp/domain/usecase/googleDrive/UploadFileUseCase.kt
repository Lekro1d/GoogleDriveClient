package com.example.googledriveapp.domain.usecase.googleDrive

import android.net.Uri
import com.example.googledriveapp.domain.repository.FilesRepository
import com.example.googledriveapp.model.googledrive.DriveResult
import javax.inject.Inject

class UploadFileUseCase @Inject constructor(private val repository: FilesRepository) {
    suspend fun uploadFile(fileUri: Uri): DriveResult {
        return repository.uploadFile(fileUri)
    }
}
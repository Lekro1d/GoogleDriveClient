package com.example.googledriveapp.domain.usecase.googleDrive

import com.example.googledriveapp.domain.repository.FilesRepository
import com.example.googledriveapp.model.googledrive.DriveResult
import com.google.api.services.drive.model.File
import javax.inject.Inject

class DownloadFileUseCase @Inject constructor(private val repository: FilesRepository) {
    suspend fun downloadFile(file: File): DriveResult{
        return repository.downloadFile(file)
    }
}
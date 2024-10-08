package com.example.googledriveapp.domain.usecase.googleDrive

import com.example.googledriveapp.domain.repository.FilesRepository
import com.example.googledriveapp.model.googledrive.DriveResult
import javax.inject.Inject

class GetFilesUseCase @Inject constructor(private val getFiles: FilesRepository) {
    suspend fun getFiles(): DriveResult {
        return getFiles.getFiles()
    }
}
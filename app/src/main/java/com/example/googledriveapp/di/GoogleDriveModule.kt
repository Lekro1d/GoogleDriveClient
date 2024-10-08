package com.example.googledriveapp.di

import com.example.googledriveapp.domain.repository.AccountRepository
import com.example.googledriveapp.domain.repository.FilesRepository
import com.example.googledriveapp.model.googleAccount.LastSignedInAccount
import com.example.googledriveapp.model.googleAccount.SignOutAccount
import com.example.googledriveapp.model.googledrive.DriveDeleteFile
import com.example.googledriveapp.model.googledrive.DriveDownloadFile
import com.example.googledriveapp.model.googledrive.DriveGetFiles
import com.example.googledriveapp.model.googledrive.DriveUploadFile
import com.example.googledriveapp.model.repository.AccountRepositoryImpl
import com.example.googledriveapp.model.repository.FilesRepositoryImpl
import dagger.Module
import dagger.Provides

@Module
class GoogleDriveModule {
    @Provides
    fun provideFilesRepository(
        getFiles: DriveGetFiles,
        downloadFile: DriveDownloadFile,
        deleteFile: DriveDeleteFile,
        uploadFile: DriveUploadFile
    ): FilesRepository {
        return FilesRepositoryImpl(
            getFiles,
            downloadFile,
            deleteFile,
            uploadFile )
    }
}
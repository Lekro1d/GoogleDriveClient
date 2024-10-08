package com.example.googledriveapp.presentation.view.fragments.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.googledriveapp.domain.usecase.googleDrive.DeleteFileUseCase
import com.example.googledriveapp.domain.usecase.googleDrive.DownloadFileUseCase
import com.example.googledriveapp.domain.usecase.googleDrive.GetFilesUseCase

class MenuViewModelFactory(
    private val getFilesDrive: GetFilesUseCase,
    private val downloadFileDrive: DownloadFileUseCase,
    private val deleteFileDrive: DeleteFileUseCase
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MenuVM(
            getFilesDrive,
            downloadFileDrive,
            deleteFileDrive
        ) as T
    }
}
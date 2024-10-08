package com.example.googledriveapp.presentation.view.fragments.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.googledriveapp.domain.usecase.googleDrive.UploadFileUseCase

class UploadViewModelFactory(
    private val uploadFile: UploadFileUseCase
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UploadVM(uploadFile) as T
    }
}
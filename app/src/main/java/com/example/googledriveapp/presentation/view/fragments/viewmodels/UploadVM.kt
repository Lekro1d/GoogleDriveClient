package com.example.googledriveapp.presentation.view.fragments.viewmodels

import android.app.Application
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googledriveapp.domain.usecase.googleDrive.UploadFileUseCase
import com.example.googledriveapp.model.googledrive.DriveResult
import com.example.googledriveapp.model.repository.FilesRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UploadVM(private val uploadFile: UploadFileUseCase): ViewModel() {

    fun uploadFile(context: Context, fileUri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = uploadFile.uploadFile(fileUri)

            withContext(Dispatchers.Main) {
                when(result) {
                    is DriveResult.FileUpload -> { Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show() }
                    is DriveResult.Error -> { Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show() }
                    else -> { Toast.makeText(context, "Ошибка приложения", Toast.LENGTH_SHORT).show() }
                }
            }
        }
    }
}
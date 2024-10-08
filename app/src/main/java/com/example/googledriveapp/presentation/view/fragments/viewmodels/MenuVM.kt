package com.example.googledriveapp.presentation.view.fragments.viewmodels

import android.content.Context
import android.content.Intent
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.googledriveapp.domain.usecase.googleDrive.DeleteFileUseCase
import com.example.googledriveapp.domain.usecase.googleDrive.DownloadFileUseCase
import com.example.googledriveapp.domain.usecase.googleDrive.GetFilesUseCase
import com.example.googledriveapp.model.googledrive.DriveResult
import com.google.api.services.drive.model.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MenuVM(
    private val getFilesDrive: GetFilesUseCase,
    private val downloadFileDrive: DownloadFileUseCase,
    private val deleteFileDrive: DeleteFileUseCase
): ViewModel() {

    private val _files = MutableLiveData<List<File>>()
    val files: LiveData<List<File>> get() = _files

    fun getFiles(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getFilesDrive.getFiles()

            withContext(Dispatchers.Main) {
                when(result) {
                    is DriveResult.GetFileCollection -> {
                        if(result.files.isNotEmpty()){
                            _files.postValue(result.files)
                        } else {
                            Toast.makeText(context, "Список пуст", Toast.LENGTH_LONG).show()
                        }
                    }
                    is DriveResult.Error -> {
                        Toast.makeText(context, "Ошибка скачивания: ${result.message}", Toast.LENGTH_LONG).show()
                    }
                    else -> Toast.makeText(context, "Ошибка приложения", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun downloadFile(context: Context, file: File) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = downloadFileDrive.downloadFile(file) // Получаем результат скачивания, далее проверяем

            withContext(Dispatchers.Main) {
                when(result) {
                    is DriveResult.FileDownloaded -> {
                        Toast.makeText(context, "Файл: ${result.file.absolutePath}, успшено скачан", Toast.LENGTH_LONG).show()

                        //Генерация URI для файла
                        val fileUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", result.file)
                        //Определеям расширение файла
                        val mimeType = getMimeType(result.file.name)

                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            setDataAndType(fileUri, mimeType)
                            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
                        }

                        context.startActivity(intent)
                    }
                    is DriveResult.Error -> {
                        Toast.makeText(context, "Ошибка скачивания: ${result.message}", Toast.LENGTH_LONG).show()
                    }
                    else -> Toast.makeText(context, "Ошибка приложения", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getMimeType(fileName: String): String {
        val extension = fileName.substringAfterLast('.', "")
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) ?: "application/octet-stream"
    }

    fun deleteFile(context: Context, file: File) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = deleteFileDrive.deleteFile(file)

            withContext(Dispatchers.Main) {
                when(result) {
                    is DriveResult.FileDeleted -> {
                        Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                    }
                    is DriveResult.Error -> {
                        Toast.makeText(context, "Ошибка удаления: ${result.message}", Toast.LENGTH_LONG).show()
                        Log.e("DriveDeleteFile", "Ошибка: ${result.message}")
                    }
                    else -> Toast.makeText(context, "Ошибка приложения", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
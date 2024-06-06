package com.example.googledriveapp.vmodel.rc

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.googledriveapp.R
import com.example.googledriveapp.databinding.RcItemFileBinding
import com.google.api.services.drive.model.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ListFileAdapter(val listener: ListenerFile) : RecyclerView.Adapter<ListFileAdapter.FileViewHolder>() {
    private val filesList = ArrayList<com.google.api.services.drive.model.File>()
    private val expandedPositions = SparseBooleanArray()

    class FileViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = RcItemFileBinding.bind(item)

        fun bind(file: com.google.api.services.drive.model.File, listener: ListenerFile, isExpanded: Boolean) = with(binding) {
            fileNameTextView.text = file.name

            // Определяем URL для иконки или миниатюры
            val imageUrl = getFileThumbnailOrIconUrl(file)

            Glide.with(fileIconImageView.context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_file) // Placeholder image while loading
                .error(R.drawable.ic_file) // Error image if URL is invalid
                .into(fileIconImageView)

            // Устанавливаем видимость дополнительной информации
            fileDetailsLayout.visibility = if (isExpanded) View.VISIBLE else View.GONE

            // Заполняем дополнительную информацию
            fileSizeTextView.text = "Память: ${file.size ?: "N/A"} bytes"
            fileMimeTypeTextView.text = "Тип MIME: ${file.mimeType}"

            val createdTime = file.createdTime?.value
            if (createdTime != null) {
                val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                val date = Date(createdTime)

                fileCreatedTimeTextView.text = "Время создания: ${dateFormat.format(date)} ${timeFormat.format(date)}"
            } else {
                fileCreatedTimeTextView.text = "Время создания: N/A"
            }

            itemView.setOnClickListener {
                listener.onClickFileItem(file, adapterPosition)
            }
        }

        private fun getFileThumbnailOrIconUrl(file: com.google.api.services.drive.model.File): String {
            return when (file.mimeType) {
                "application/pdf" -> "ic_pdf"
                "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> "ic_file"
                "image/jpeg", "image/png", "image/gif" -> file.thumbnailLink ?: "ic_file"
                else -> "ic_file" // Default icon URL
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rc_item_file, parent, false)
        return FileViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val isExpanded = expandedPositions.get(position, false)
        holder.bind(filesList[position], listener, isExpanded)
    }

    override fun getItemCount(): Int {
        return filesList.size
    }

    // Метод для добавления списка файлов
    fun addFiles(files: List<com.google.api.services.drive.model.File>) {
        filesList.clear()
        filesList.addAll(files)
        notifyDataSetChanged()
    }

    // Метод для получения файла по позиции
    fun getFileAtPosition(position: Int): com.google.api.services.drive.model.File {
        return filesList[position]
    }

    // Метод для удаления файла по позиции
    fun removeFileAtPosition(position: Int) {
        filesList.removeAt(position)
        notifyItemRemoved(position)
    }

    // Метод для переключения состояния расширения элемента списка
    fun toggleExpansion(position: Int) {
        val isExpanded = expandedPositions.get(position, false)
        if (isExpanded) {
            expandedPositions.delete(position)
        } else {
            expandedPositions.put(position, true)
        }
        notifyItemChanged(position)
    }

    // Интерфейс для обработки кликов на элементы списка
    interface ListenerFile {
        fun onClickFileItem(file: com.google.api.services.drive.model.File, position: Int)
    }
}
package com.example.googledriveapp.vmodel.rc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.googledriveapp.R
import com.example.googledriveapp.databinding.RcItemFileBinding
import com.google.api.services.drive.model.File

class ListFileAdapter(val listener: ListenerFile): RecyclerView.Adapter<ListFileAdapter.FileViewHolder>() {
    private val filesList = ArrayList<com.google.api.services.drive.model.File>()

    class FileViewHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = RcItemFileBinding.bind(item)

        fun bind(file: com.google.api.services.drive.model.File, listener: ListenerFile) = with(binding) {
            fileNameTextView.text = file.name

            // Определяем URL для иконки или миниатюры
            val imageUrl = getFileThumbnailOrIconUrl(file)

            Glide.with(fileIconImageView.context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_file) // Placeholder image while loading
                .error(R.drawable.ic_file) // Error image if URL is invalid
                .into(fileIconImageView)

            itemView.setOnClickListener {
                listener.onClickFileItem(file)
            }
        }

        private fun getFileThumbnailOrIconUrl(file: com.google.api.services.drive.model.File): String {
            return when (file.mimeType) {
                "application/pdf" -> "ic_pdf"
                "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> "ic_file"
                "image/jpeg", "image/png", "image/gif" -> file.thumbnailLink ?: "ic_file"
                // Add more MIME type cases as needed
                else -> "ic_file" // Default icon URL
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rc_item_file, parent, false)
        return FileViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.bind(filesList[position], listener)
    }

    override fun getItemCount(): Int {
        return filesList.size
    }

    fun addFiles(files: List<com.google.api.services.drive.model.File>) {
        filesList.clear()
        filesList.addAll(files)
        notifyDataSetChanged()
    }



    interface ListenerFile {
        fun onClickFileItem(file: com.google.api.services.drive.model.File)
    }
}
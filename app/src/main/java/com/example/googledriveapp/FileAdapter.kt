package com.example.googledriveapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.googledriveapp.databinding.RcItemFileBinding
import com.google.api.services.drive.model.File

class FileAdapter(val listener: ListenerFile): RecyclerView.Adapter<FileAdapter.FileViewHolder>() {
    private val filesList = ArrayList<com.google.api.services.drive.model.File>()

    class FileViewHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = RcItemFileBinding.bind(item)

        fun bind(file: File, listener: ListenerFile) = with(binding) {
            fileNameTextView.text = file.name
            // TODO: Load the icon based on file MIME type or use a default icon
            fileIconImageView.setImageResource(getFileIconResource(file.mimeType))
            itemView.setOnClickListener {
                listener.onClickFileItem(file)
            }
        }
        private fun getFileIconResource(mimeType: String?): Int {
            return when (mimeType) {
                "application/pdf" -> R.drawable.ic_pdf // Example for PDF
                "image/jpeg" -> R.drawable.ic_image // Example for JPEG image
                // Add more MIME type cases as needed
                else -> R.drawable.ic_file // Default file icon
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
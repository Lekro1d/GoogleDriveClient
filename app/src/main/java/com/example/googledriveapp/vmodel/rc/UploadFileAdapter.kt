package com.example.googledriveapp.vmodel.rc

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.googledriveapp.R
import com.example.googledriveapp.databinding.RcUploadFileItemBinding

class UploadFileAdapter: RecyclerView.Adapter<UploadFileAdapter.UploadFileViewHolder>() {
    private val filesList = ArrayList<Uri>()
    class UploadFileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = RcUploadFileItemBinding.bind(itemView)
        fun bind(file: Uri) = with(binding) {
            textView.text = file.lastPathSegment
            Glide.with(itemView.context)
                .load(file)
                .into(imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadFileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rc_upload_file_item, parent, false)
        return UploadFileViewHolder(view)
    }

    override fun onBindViewHolder(holder: UploadFileViewHolder, position: Int) {
        holder.bind(filesList[position])
    }

    override fun getItemCount(): Int {
        return filesList.size
    }

    fun setFiles(files: ArrayList<Uri>) {
        filesList.clear()
        filesList.addAll(files)
        notifyDataSetChanged()
    }
}
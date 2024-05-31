package com.example.googledriveapp.view.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.googledriveapp.R
import com.example.googledriveapp.databinding.FragmentUploadBinding
import com.example.googledriveapp.vmodel.UploadViewModel
import com.example.googledriveapp.vmodel.rc.UploadFileAdapter

class UploadFragment : Fragment() {
    private var _binding: FragmentUploadBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: UploadViewModel

    private lateinit var uploadFileAdapter: UploadFileAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUploadBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(this).get(UploadViewModel::class.java)

        viewModel.googleDriveService(requireContext())

        uploadFileAdapter = UploadFileAdapter()

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = uploadFileAdapter

        val files = arguments?.getParcelableArrayList<Uri>("selected_files")
        if (files != null) {
            uploadFileAdapter.setFiles(files)
        }

        binding.btnUpload.setOnClickListener {
            files?.forEach { fileUri ->
                viewModel.uploadImageFile(fileUri, requireContext())
                findNavController().navigate(R.id.action_uploadFragment_to_menuFragment)
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
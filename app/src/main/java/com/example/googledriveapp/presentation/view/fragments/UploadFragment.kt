package com.example.googledriveapp.presentation.view.fragments

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
import com.example.googledriveapp.domain.usecase.googleDrive.UploadFileUseCase
import com.example.googledriveapp.di.app.App
import com.example.googledriveapp.presentation.view.fragments.viewmodels.UploadVM
import com.example.googledriveapp.presentation.view.fragments.viewmodels.UploadViewModelFactory
import com.example.googledriveapp.presentation.view.rc.UploadFileAdapter
import javax.inject.Inject

class UploadFragment : Fragment() {
    private var _binding: FragmentUploadBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: UploadVM
    lateinit var factory: UploadViewModelFactory

    private lateinit var uploadFileAdapter: UploadFileAdapter

    @Inject
    lateinit var driveUploadFile: UploadFileUseCase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUploadBinding.inflate(inflater, container, false)
        val view = binding.root

        (requireActivity().application as App).appComponent.inject(this)

        factory = UploadViewModelFactory(driveUploadFile)
        viewModel = ViewModelProvider(this, factory).get(UploadVM::class.java)

        uploadFileAdapter = UploadFileAdapter()

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = uploadFileAdapter

        val files = arguments?.getParcelableArrayList<Uri>("selected_files")
        if (files != null) {
            uploadFileAdapter.setFiles(files)
        }

        binding.btnUpload.setOnClickListener {
            files?.forEach { fileUri ->
                viewModel.uploadFile(requireContext(), fileUri)
            }
            binding.btnBack.visibility = View.VISIBLE
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_uploadFragment_to_menuFragment)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
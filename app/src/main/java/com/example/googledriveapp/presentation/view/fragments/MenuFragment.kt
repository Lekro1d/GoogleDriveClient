package com.example.googledriveapp.presentation.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.googledriveapp.databinding.FragmentMenuBinding
import com.example.googledriveapp.domain.usecase.googleDrive.DeleteFileUseCase
import com.example.googledriveapp.domain.usecase.googleDrive.DownloadFileUseCase
import com.example.googledriveapp.domain.usecase.googleDrive.GetFilesUseCase
import com.example.googledriveapp.di.app.App
import com.example.googledriveapp.presentation.view.fragments.viewmodels.MenuVM
import com.example.googledriveapp.presentation.view.fragments.viewmodels.MenuViewModelFactory
import com.example.googledriveapp.presentation.view.rc.ListFileAdapter
import com.example.googledriveapp.presentation.view.rc.SwipeToDeleteCallback
import com.google.api.services.drive.model.File
import java.util.Locale
import javax.inject.Inject

class MenuFragment : Fragment() {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MenuVM
    lateinit var factory: MenuViewModelFactory

    private lateinit var fileAdapter: ListFileAdapter

    private var allFiles = ArrayList<File>()
    private var filteredFiles = ArrayList<File>()

    private lateinit var searchView: SearchView

    @Inject
    lateinit var driveGetFiles: GetFilesUseCase

    @Inject
    lateinit var driveDownloadFile: DownloadFileUseCase

    @Inject
    lateinit var driveDeleteFile: DeleteFileUseCase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        val view = binding.root

        (requireActivity().application as App).appComponent.inject(this)

        factory = MenuViewModelFactory(driveGetFiles, driveDownloadFile, driveDeleteFile)
        viewModel = ViewModelProvider(this, factory).get(MenuVM::class.java)

        fileAdapter = ListFileAdapter(object : ListFileAdapter.ListenerFile {
            override fun onClickFileItem(file: File, position: Int) {
                fileAdapter.toggleExpansion(position)
            }
        })

        binding.rcvFiles.layoutManager = LinearLayoutManager(requireContext())
        binding.rcvFiles.adapter = fileAdapter

        viewModel.getFiles(requireContext())

        viewModel.files.observe(viewLifecycleOwner) { files ->
            allFiles.clear()
            allFiles.addAll(files)
            filterFiles("")
        }

        setupSearchView()

        val swipeCallback = SwipeToDeleteCallback(object : SwipeToDeleteCallback.SwipeListener {
            override fun onItemSwipedLeft(position: Int) {
                val file = fileAdapter.getFileAtPosition(position)
                viewModel.downloadFile(requireContext(), file)
                fileAdapter.notifyItemChanged(position)
            }

            override fun onItemSwipedRight(position: Int) {
                val file = fileAdapter.getFileAtPosition(position)
                viewModel.deleteFile(requireContext(), file)
                fileAdapter.removeFileAtPosition(position)
            }
        })

        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(binding.rcvFiles)

        return view
    }

    private fun setupSearchView() {
        searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterFiles(newText)
                return true
            }
        })
    }

    private fun filterFiles(query: String?) {
        val searchText = query?.lowercase(Locale.getDefault()).orEmpty()
        filteredFiles.clear()

        if (searchText.isEmpty()) {
            filteredFiles.addAll(allFiles)
        } else {
            for (file in allFiles) {
                if (file.name.lowercase(Locale.getDefault()).contains(searchText)) {
                    filteredFiles.add(file)
                }
            }
        }

        fileAdapter.addFiles(filteredFiles)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
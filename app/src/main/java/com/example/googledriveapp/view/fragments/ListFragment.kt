package com.example.googledriveapp.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.googledriveapp.vmodel.rc.ListFileAdapter
import com.example.googledriveapp.databinding.FragmentMenuBinding
import com.example.googledriveapp.vmodel.ListViewModel
import com.google.api.services.drive.model.File
import java.util.Locale

class ListFragment : Fragment() {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ListViewModel

    private lateinit var fileAdapter: ListFileAdapter

    private lateinit var searchView: SearchView
    private var allFiles = ArrayList<com.google.api.services.drive.model.File>()
    private var filteredFiles = ArrayList<com.google.api.services.drive.model.File>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)

        viewModel.googleDriveService(requireContext())

        //слушатель на каждый элемент recycler view
        fileAdapter = ListFileAdapter(object : ListFileAdapter.ListenerFile {
            override fun onClickFileItem(file: File) {
                viewModel.downloadFile(requireContext(), file)
            }
        })

        //инициализация recyclerview
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = fileAdapter

        //вывести все файлы из google drive
        viewModel.listFiles(requireContext())

        viewModel.files.observe(viewLifecycleOwner) { files ->
            allFiles.clear()
            allFiles.addAll(files)
            filterFiles("")
        }

        setupSearchView()

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
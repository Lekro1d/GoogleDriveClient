package com.example.googledriveapp.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.googledriveapp.databinding.FragmentEditBinding
import com.example.googledriveapp.vmodel.EditViewModel

class EditFragment : Fragment() {
    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: EditViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(this).get(EditViewModel::class.java)

        viewModel.googleDriveService(requireContext())

        //кнопка добавить текст в google drive
        binding.btnAddTxt.setOnClickListener {
            val fileName = binding.titleText.text.toString()
            val fileContent = binding.descText.text.toString()
            viewModel.uploadTextFile(fileName, fileContent, requireContext())

            binding.titleText.text.clear()
            binding.descText.text.clear()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
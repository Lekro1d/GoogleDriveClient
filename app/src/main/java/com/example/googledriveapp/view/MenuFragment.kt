package com.example.googledriveapp.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.googledriveapp.FileAdapter
import com.example.googledriveapp.LoginActivity
import com.example.googledriveapp.R
import com.example.googledriveapp.databinding.FragmentMenuBinding
import com.example.googledriveapp.vmodel.MenuViewModel
import com.google.api.services.drive.model.File

class MenuFragment : Fragment() {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MenuViewModel

    private lateinit var fileAdapter: FileAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(this).get(MenuViewModel::class.java)

        init()

        //кнопка чтобы выйти из аккаунта
        binding.btnSignOut.setOnClickListener {
            viewModel.signOut {
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }

        //кнопка добавить текст в google drive
        binding.btnAddTxt.setOnClickListener {
            val fileName = "example.txt"
            val fileContent = "This is a sample text file."
            viewModel.uploadTextFile(fileName, fileContent, requireContext())
        }

        //слушатель на каждый элемент recycler view
        fileAdapter = FileAdapter(object : FileAdapter.ListenerFile {
            override fun onClickFileItem(file: com.google.api.services.drive.model.File) {
                //
            }
        })

        //инициализация recyclerview
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = fileAdapter

        //кнопка чтобы вывести все файлы из google drive !доработать!
        binding.btnShowFiles.setOnClickListener {
            viewModel.listFiles(requireContext())
        }

        viewModel.files.observe(viewLifecycleOwner) { files ->
            fileAdapter.addFiles(files)
        }

        return view
    }

    private fun init() {
        //инициализация sign-in и google drive
        viewModel.initializeGoogleSignInClient(requireContext(), getString(R.string.default_web_client_id))
        viewModel.googleDriveService(requireContext())

        //получение почты, автарки и имя
        viewModel.userName.observe(viewLifecycleOwner) { name ->
            binding.nameTextView.text = name
        }

        viewModel.userEmail.observe(viewLifecycleOwner) { email ->
            binding.emailTextView.text = email
        }

        viewModel.userPhotoUrl.observe(viewLifecycleOwner) { photoUrl ->
            if (photoUrl != null) {
                Glide.with(this)
                    .load(photoUrl)
                    .circleCrop()
                    .into(binding.userImageView)
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
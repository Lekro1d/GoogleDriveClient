package com.example.googledriveapp.view.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.googledriveapp.R
import com.example.googledriveapp.databinding.FragmentAccountBinding
import com.example.googledriveapp.view.activity.LoginActivity
import com.example.googledriveapp.vmodel.AccountViewModel

class AccountFragment : Fragment() {
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(this).get(AccountViewModel::class.java)

        init()

        //кнопка чтобы выйти из аккаунта
        binding.btnSignOut.setOnClickListener {
            viewModel.signOut {
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }

        return view
    }

    private fun init() {
        //инициализация sign-in
        viewModel.initializeGoogleSignInClient(requireContext(), getString(R.string.default_web_client_id))

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
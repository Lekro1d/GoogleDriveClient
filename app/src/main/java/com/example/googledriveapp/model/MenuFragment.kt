package com.example.googledriveapp.model

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.googledriveapp.LoginActivity
import com.example.googledriveapp.R
import com.example.googledriveapp.databinding.FragmentMenuBinding
import com.google.firebase.auth.FirebaseAuth

class MenuFragment : Fragment() {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        val view = binding.root

        val name = arguments?.getString("name")
        val email = arguments?.getString("email")

        binding.emailTextView.text = email
        binding.nameTextView.text = name

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
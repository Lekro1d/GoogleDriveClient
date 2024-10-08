package com.example.googledriveapp.presentation.view.activity

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.googledriveapp.R
import com.example.googledriveapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // Константа для кода запроса разрешения
    private val PERMISSION_REQUEST_CODE = 1
    // Константа для кода запроса выбора файла
    private val FILE_SELECT_CODE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupNavigation()
        binding.fab.setOnClickListener { showBottomDialog() }
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavView.setupWithNavController(navController)
    }

    // Метод для показа диалога в нижней части экрана
    private fun showBottomDialog() {
        val dialog = Dialog(this).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.bottomsheet_layout)
        }

        with(dialog) {
            findViewById<LinearLayout>(R.id.layoutFiles).setOnClickListener {
                dismiss()
                openFileChooser()
            }

            findViewById<LinearLayout>(R.id.layoutMedia).setOnClickListener {
                dismiss()
                showToast("Галерея")
            }

            findViewById<LinearLayout>(R.id.layoutCamera).setOnClickListener {
                dismiss()
                showToast("Камера")
            }

            findViewById<ImageView>(R.id.cancelButton).setOnClickListener { dismiss() }

            window?.apply {
                setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                attributes.windowAnimations = R.style.DialogAnimation
                setGravity(Gravity.BOTTOM)
            }
            show()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Метод для открытия выборщика файлов
    private fun openFileChooser() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, FILE_SELECT_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_SELECT_CODE && resultCode == Activity.RESULT_OK) {
            val files = mutableListOf<Uri>()
            data?.data?.let { uri ->
                files.add(uri)
            }
            data?.clipData?.let { clipData ->
                for (i in 0 until clipData.itemCount) {
                    files.add(clipData.getItemAt(i).uri)
                }
            }
            val bundle = Bundle().apply {
                putParcelableArrayList("selected_files", ArrayList(files))
            }
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            navHostFragment.navController.navigate(R.id.uploadFragment, bundle)
        }
    }
}
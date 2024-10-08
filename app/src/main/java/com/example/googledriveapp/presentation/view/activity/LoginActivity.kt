package com.example.googledriveapp.presentation.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.googledriveapp.R
import com.example.googledriveapp.databinding.ActivityLoginBinding
import com.example.googledriveapp.domain.usecase.googleAccount.SignInUseCase
import com.example.googledriveapp.di.app.App
import com.example.googledriveapp.model.googleAccount.LoginState
import com.example.googledriveapp.presentation.view.activity.viewmodels.LoginVM
import com.example.googledriveapp.presentation.view.activity.viewmodels.LoginViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.google.api.services.drive.DriveScopes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val factory: LoginViewModelFactory by lazy {
        LoginViewModelFactory(signInUseCase)
    }

    private val viewModel: LoginVM by lazy {
        ViewModelProvider(this, factory).get(LoginVM::class.java)
    }

    private val reqCode: Int = 123

    @Inject
    lateinit var signInUseCase: SignInUseCase

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (applicationContext as? App)?.appComponent?.inject(this)

        firebaseAuth.currentUser?.let {
            successfulLogin()
        }

        binding.signIn.setOnClickListener {
            signInWithGoogle()
        }

        viewModel.loginState.observe(this) { state ->
            when (state) {
                is LoginState.SuccessSignIn -> {
                    Toast.makeText(this, "Авторизация прошла успешно", Toast.LENGTH_LONG).show()
                    successfulLogin()
                }
                is LoginState.ErrorSignIn -> {
                    Toast.makeText(this, "Ошибка авторизации", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    //Метод для инициирования процесса входа в Google
    private fun signInWithGoogle() {
        val signInIntent: Intent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, reqCode)
    }

    //Функция для обработки результатов активности
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == reqCode) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    //Функция для обработки результата авторизации
    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            account?.let {
                viewModel.signIn(it.idToken ?: "")
            }
        } catch (e: ApiException) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }
    //Функция, который вызывается при успешной авторизации
    private fun successfulLogin() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
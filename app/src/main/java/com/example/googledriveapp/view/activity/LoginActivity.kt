package com.example.googledriveapp.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.googledriveapp.R
import com.example.googledriveapp.databinding.ActivityLoginBinding
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

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth  // Инициализация Firebase аутентификации
    private lateinit var googleSignInClient: GoogleSignInClient  // Инициализация клиента для входа через Google

    private val reqCode: Int = 123  // Код запроса для Google Sign-In

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Получение экземпляра FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        // Настройка опций для входа через Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .requestScopes(Scope(DriveScopes.DRIVE_FILE))
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Кнопка для прохождения авторизации
        binding.signIn.setOnClickListener {
            Toast.makeText(this, "Logging In", Toast.LENGTH_SHORT).show()
            signInWithGoogle()
        }

        // Проверка, есть ли текущий пользователь, и перенаправление при успешном входе
        firebaseAuth.currentUser?.let {
            successfulLogin()
        }
    }

    // Метод для инициирования процесса входа через Google
    private fun signInWithGoogle() {
        val signInIntent: Intent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, reqCode)
    }

    // Обработка результата активности для входа через Google
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == reqCode) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    // Обработка результата задачи входа через Google
    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            account?.let { updateUI(it) }
        } catch (e: ApiException) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    // Обновление UI и Firebase аутентификация с использованием учетных данных Google
    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        //Если аутентификация прошла успешна, то пользователь переходит в MainActivity
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Метод для обработки успешного входа пользователя
    private fun successfulLogin() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
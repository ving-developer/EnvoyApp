package com.example.envoyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import com.example.envoyapp.databinding.ActivityLoginBinding
import com.example.envoyapp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.submitButtonLogin.setOnClickListener {
            val email = binding.emailEdittextLogin.text.toString()
            val password = binding.passwordEdittextLogin.text.toString()
            Log.d("LoginActivity","Attempt login with email/pw $email/***")
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener {
                    if(!it.isSuccessful) return@addOnCompleteListener
                    Log.d("MainActivity", "created user with uid: ${it.result.user?.uid}")
                }
                .addOnFailureListener {
                    Log.d("MainActivity", "Failed to create user ${it.message}")
                };
        }

        binding.backToRegisterTextviewLogin.setOnClickListener {
            finish()
        }
    }
}
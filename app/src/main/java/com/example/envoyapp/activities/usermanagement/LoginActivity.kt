package com.example.envoyapp.activities.usermanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.envoyapp.activities.messages.LatestMessagesActivity
import com.example.envoyapp.databinding.ActivityLoginBinding
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
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener {
                    if(!it.isSuccessful) return@addOnCompleteListener
                    val intent = Intent(this, LatestMessagesActivity::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener {
                    binding.layoutEmailEdittextLogin.isErrorEnabled = true
                    binding.layoutEmailEdittextLogin.error = "Email incorrect"
                    binding.layoutPasswordEdittextLogin.isErrorEnabled = true
                    binding.layoutPasswordEdittextLogin.error = "Password incorrect"
                };
        }

        binding.backToRegisterTextviewLogin.setOnClickListener {
            finish()
        }
    }
}
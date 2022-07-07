package com.example.envoyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        submit_button_login.setOnClickListener {
            val email = email_edittext_login.text.toString()
            val password = password_edittext_login.text.toString()
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

        back_to_register_textview_login.setOnClickListener {
            finish()
        }
    }
}
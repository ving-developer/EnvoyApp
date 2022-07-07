package com.example.envoyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import  kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        register_button_register.setOnClickListener {
            performRegister();
        }
        alredy_have_account_text_view_register.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        select_profile_photo_button_register.setOnClickListener {

        }
    }

    private fun performRegister() {
        val email = email_edittext_register.text.toString()
        val password = password_edittext_register.text.toString()
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this,"Please enter text in email/password", Toast.LENGTH_SHORT).show()
            return
        }
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(!it.isSuccessful) return@addOnCompleteListener
                Log.d("MainActivity", "created user with uid: ${it.result.user?.uid}")
            }
            .addOnFailureListener {
                Log.d("MainActivity", "Failed to create user ${it.message}")
                Toast.makeText(this,"Failed to create user ${it.message}",Toast.LENGTH_LONG).show()
            };
    }
}
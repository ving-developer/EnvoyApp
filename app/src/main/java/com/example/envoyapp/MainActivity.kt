package com.example.envoyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import  kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        register_button_register.setOnClickListener {
            val email = username_edittext_register.text.toString()
            val password = password_edittext_register.text.toString()
            Log.d("MainActivity","Email: "+ email)
            Log.d("MainActivity","Password: $password")
        }
        alredy_have_account_text_view_register.setOnClickListener {
            Log.d("MainActivity","Try to show login activity")
        }
    }
}
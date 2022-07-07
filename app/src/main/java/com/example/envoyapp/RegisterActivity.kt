package com.example.envoyapp

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import  kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {
    var selectedPhotoUri: Uri? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_button_register.setOnClickListener {
            performRegister();
        }
        alredy_have_account_text_view_register.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        select_profile_photo_button_register.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            Log.d("RegisterActivity","Photo was selected")
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri)
            selectedphoto_image_view_register.setImageBitmap(bitmap)
            select_profile_photo_button_register.alpha = 0f
        }
    }

    private fun performRegister() {
        val email = email_edittext_register.text.toString()
        val password = password_edittext_register.text.toString()
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this,"Please enter text in username/email/password", Toast.LENGTH_SHORT).show()
            return
        }
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(!it.isSuccessful) return@addOnCompleteListener
                Log.d("RegisterActivity", "created user with uid: ${it.result.user?.uid}")
                uploadImageToFirebaseStore()
            }
            .addOnFailureListener {
                Log.d("RegisterActivity", "Failed to create user ${it.message}")
                Toast.makeText(this,"Failed to create user ${it.message}",Toast.LENGTH_LONG).show()
            };
    }

    private fun uploadImageToFirebaseStore() {
        if(selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("RegisterActivity","Success with upload image: ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {
                    Log.d("RegisterActivity","File location: $it")
                    saveUserToFirebaseDatabase(it.toString())
                }
            }
            .addOnFailureListener {
                Log.d("RegisterActivity", "Failed to upload image ${it.message}")
            };
    }

    private fun saveUserToFirebaseDatabase(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid,username_edittext_register.text.toString(), profileImageUrl)
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "User has been saved in Firebase Database")
            }
            .addOnFailureListener {
                Log.d("RegisterActivity", "Failed to save user in Database: ${it.message}")
            };
    }
}

class User(val uid: String, val username: String, val profileImageUrl: String)
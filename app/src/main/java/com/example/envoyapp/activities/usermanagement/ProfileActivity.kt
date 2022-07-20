package com.example.envoyapp.activities.usermanagement

import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.envoyapp.R
import com.example.envoyapp.RegisterActivity
import com.example.envoyapp.activities.messages.LatestMessagesActivity
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*


class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_profile)
        window.setBackgroundDrawableResource(android.R.color.transparent);
        logout_button_profile.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        updateProfile()
    }

    private fun updateProfile() {
        val user = LatestMessagesActivity.currentUser
        username_textview_profile.text = user?.username
        status_textview_profile.text = user?.status
        val imageUrl = LatestMessagesActivity.currentUser?.profileImageUrl ?: ""
        if(imageUrl.isNotEmpty()){
            Picasso.get().load(imageUrl).into(profile_image_view_profile)
            profile_logo_profile.alpha = 0f
        }
    }
}
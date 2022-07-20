package com.example.envoyapp.activities.messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.envoyapp.activities.usermanagement.ProfileActivity
import com.example.envoyapp.R
import com.example.envoyapp.RegisterActivity
import com.example.envoyapp.User
import com.example.envoyapp.databinding.ActivityLatestMessagesBinding
import com.example.envoyapp.views.ChatMessage
import com.example.envoyapp.views.LatestMessageRow
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieAdapter

class LatestMessagesActivity : AppCompatActivity() {
    companion object {
        var currentUser: User? = null
    }
    val adapter = GroupieAdapter()
    val latestMessageMap = HashMap<String, ChatMessage>()
    private lateinit var binding: ActivityLatestMessagesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLatestMessagesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val toolbarFinded = binding.includeToolbarLatestMessage.appToolbar
        setSupportActionBar(toolbarFinded)
        binding.recyclerviewLatestMessages.adapter = adapter
        binding.recyclerviewLatestMessages.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        adapter.setOnItemClickListener { item, view ->
            val row = item as LatestMessageRow
            val intent = Intent(this, ChatLogActivity::class.java)
            intent.putExtra(NewMessageActivity.USER_KEY,row.chatPartnerUser)
            startActivity(intent)
        }
        binding.floatingButtonLatestMessages.setOnClickListener{
            val intent = Intent(this, NewMessageActivity::class.java)
            startActivity(intent)
        }
        binding.includeToolbarLatestMessage.profileLogoToolbar.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        verifyUserLoggedIn()
        fetchCurrentUser()
        listendToLatestMessages()
    }

    private fun listendToLatestMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
        ref.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java) ?: return
                latestMessageMap[snapshot.key!!] = chatMessage
                refreshRecyclerViewMessages()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java) ?: return
                latestMessageMap[snapshot.key!!] = chatMessage
                refreshRecyclerViewMessages()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) { }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) { }

            override fun onCancelled(error: DatabaseError) { }
        })
    }

    private fun refreshRecyclerViewMessages() {
        adapter.clear()
        latestMessageMap.values.forEach {
            adapter.add(LatestMessageRow(it))
        }
    }

    private fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(User::class.java)
                val imageUrl = currentUser?.profileImageUrl ?: ""
                if(imageUrl.isNotEmpty()){
                    Picasso.get().load(imageUrl).into(binding.includeToolbarLatestMessage.profileImageViewToolbar)
                    binding.includeToolbarLatestMessage.profileLogoToolbar.alpha = 0f
                }
            }

            override fun onCancelled(error: DatabaseError) { }
        })
    }

    private fun verifyUserLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if(uid == null){
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
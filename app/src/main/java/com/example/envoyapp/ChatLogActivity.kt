package com.example.envoyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.envoyapp.databinding.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.viewbinding.BindableItem
import java.sql.Timestamp

class ChatLogActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatLogBinding
    val adapter = GroupieAdapter()
    var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatLogBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = user?.username
        binding.recyclerviewChatLog.adapter = adapter
        listenForMessages()
        binding.sendButtonChatlog.setOnClickListener {
            performSendMessage()
        }
    }

    private fun listenForMessages() {
        val ref = FirebaseDatabase.getInstance().getReference("/messages")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)
                if(chatMessage != null){
                    Log.d("ChatLogActivity",chatMessage.text)
                    if(chatMessage.fromId == FirebaseAuth.getInstance().uid){
                        val currentUser = LatestMessagesActivity.currentUser
                        adapter.add(ChatToItem(chatMessage.text, currentUser!!))
                    }else{
                        adapter.add(ChatFromItem(chatMessage.text,user!!))
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun performSendMessage() {
        val text = binding.edittextChatLog.text.toString()
        val fromId = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val toId = user!!.uid
        val reference = FirebaseDatabase.getInstance().getReference("/messages").push()
        if(fromId == null) return
        val chatMessage = ChatMessage(reference.key!!, text, fromId, toId, System.currentTimeMillis()/1000)
        reference.setValue(chatMessage).addOnSuccessListener {
            Log.d("ChatLogActivity","Message has been sent")
        }
    }
}

class ChatFromItem(val text: String, val user: User) : BindableItem<ChatFromRowBinding>() {
    override fun bind(viewBinding: ChatFromRowBinding, position: Int) {
        viewBinding.textViewFromRow.text = text
        val uri = user.profileImageUrl
        Picasso.get().load(uri).into(viewBinding.circleImageviewFromRow)
        viewBinding.circleImageviewFromRow
    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }

    override fun initializeViewBinding(view: View): ChatFromRowBinding {
        return ChatFromRowBinding.bind(view)
    }
}

class ChatToItem(val text: String, val user: User) : BindableItem<ChatToRowBinding>() {
    override fun bind(viewBinding: ChatToRowBinding, position: Int) {
        viewBinding.textviewToRow.text = text
        val uri = user.profileImageUrl
        Picasso.get().load(uri).into(viewBinding.circleImageviewToRow)
        viewBinding.circleImageviewToRow
    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }

    override fun initializeViewBinding(view: View): ChatToRowBinding {
        return ChatToRowBinding.bind(view)
    }
}

class ChatMessage(val id: String, val text: String, val fromId: String, val toId: String, val timestamp: Long){
    constructor(): this("","","","",-1)
}
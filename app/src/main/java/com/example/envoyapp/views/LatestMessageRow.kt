package com.example.envoyapp.views

import android.view.View
import com.example.envoyapp.R
import com.example.envoyapp.User
import com.example.envoyapp.databinding.LatestMessageRowBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.viewbinding.BindableItem

class LatestMessageRow(val chatMessage: ChatMessage) : BindableItem<LatestMessageRowBinding>() {
    var chatPartnerUser: User? = null

    override fun bind(viewBinding: LatestMessageRowBinding, position: Int) {
        viewBinding.messageTextviewLatestMessage.text = chatMessage.text
        var chatPartnerId = if(chatMessage.fromId == FirebaseAuth.getInstance().uid){
            chatMessage.toId
        }else{
            chatMessage.fromId
        }
        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatPartnerUser = snapshot.getValue(User::class.java)
                viewBinding.usernameTextviewLatestMessage.text = chatPartnerUser?.username
                Picasso.get().load(chatPartnerUser?.profileImageUrl).into(viewBinding.profileImageviewLatestMessage)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun getLayout(): Int {
        return R.layout.latest_message_row
    }

    override fun initializeViewBinding(view: View): LatestMessageRowBinding {
        return LatestMessageRowBinding.bind(view)
    }

}
package com.example.envoyapp.views

import android.view.View
import com.example.envoyapp.R
import com.example.envoyapp.User
import com.example.envoyapp.databinding.ChatFromRowBinding
import com.example.envoyapp.databinding.ChatToRowBinding
import com.squareup.picasso.Picasso
import com.xwray.groupie.viewbinding.BindableItem

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
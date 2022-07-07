package com.example.envoyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.envoyapp.databinding.ActivityNewMessageBinding
import com.example.envoyapp.databinding.UserRowNewMessageBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.viewbinding.BindableItem

class NewMessageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewMessageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewMessageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Select User"

        fetchUsers()
    }

    companion object {
        val USER_KEY = "USER_KEY"
    }

    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupieAdapter()
                snapshot.children.forEach {
                    Log.d("NewMessageActivity",it.toString())
                    val user = it.getValue(User::class.java)
                    if(user != null){
                        adapter.add(UserItem(user))
                    }
                }
                adapter.setOnItemClickListener {
                    item, view ->
                        val userItem = item as UserItem
                        val intent = Intent(view.context,ChatLogActivity::class.java)
                        intent.putExtra(USER_KEY,userItem.user)
                        startActivity(intent)
                        finish()
                }
                binding.recyclerviewNewmessage.adapter = adapter
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}

class UserItem(val user: User) : BindableItem<UserRowNewMessageBinding>() {
    override fun bind(viewBinding: UserRowNewMessageBinding, position: Int) {
        viewBinding.usernameTextviewNewmessage.text = user.username
        Picasso.get().load(user.profileImageUrl).into( viewBinding.imageviewNewmessage)
    }

    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }

    override fun initializeViewBinding(view: View): UserRowNewMessageBinding {
        return UserRowNewMessageBinding.bind(view)
    }


}
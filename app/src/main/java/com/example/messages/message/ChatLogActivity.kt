package com.example.messages.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.messages.R
import com.example.messages.model.ChatMessage
import com.example.messages.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatLogActivity : AppCompatActivity() {

    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)
        recycleViewChatLog.layoutManager = LinearLayoutManager(this)

        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = user.username

        recycleViewChatLog.adapter = adapter

        listenForMessages()

        sendButtonChatLog.setOnClickListener {
            performSendMessage()
        }
    }

    private fun performSendMessage(){
        val reference = FirebaseDatabase.getInstance().getReference("/messages").push()
        val id = reference.key
        val text = editTextChatLog.text.toString()
        val fromId = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val toId = user.uid

        if(fromId == null || id== null) return

        val chatMessage = ChatMessage(id, text,fromId, toId,System.currentTimeMillis())

        reference.setValue(chatMessage).addOnSuccessListener {
            Log.d("ChatLogActivity", "Message ${reference.key} Saved in DB")
        }

    }

    private fun listenForMessages() {
        val ref = FirebaseDatabase.getInstance().getReference("/messages")

        ref.addChildEventListener(object: ChildEventListener {

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)

                if (chatMessage != null) {
                    Log.d("ChatLogActivity", chatMessage.text)

                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
                        adapter.add(ChatToItem(chatMessage.text))
                    } else {
                        adapter.add(ChatFromItem(chatMessage.text))
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }
        })
    }
}

class ChatFromItem(private val text: String): Item<ViewHolder> () {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textViewChatFrom.text = text
    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}

class ChatToItem(private val text: String): Item<ViewHolder> () {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textViewChatTo.text = text
    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}

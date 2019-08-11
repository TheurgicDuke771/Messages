package com.example.messages.message

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.messages.R
import com.example.messages.model.ChatMessage
import com.example.messages.model.User
import com.example.messages.views.ChatFromItem
import com.example.messages.views.ChatToItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*

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
        val text = editTextChatLog.text.toString()
        val fromId = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val toId = user.uid
        val reference = FirebaseDatabase.getInstance().getReference("/userMessages/$fromId/$toId").push()
        val toReference = FirebaseDatabase.getInstance().getReference("/userMessages/$toId/$fromId").push()
        val id = reference.key

        if(fromId == null || id== null) return

        val chatMessage = ChatMessage(id, text,fromId, toId,System.currentTimeMillis())

        reference.setValue(chatMessage).addOnSuccessListener {
            Log.d("ChatLogActivity", "Message ${reference.key} Saved in DB")
            editTextChatLog.text.clear()
            recycleViewChatLog.scrollToPosition(adapter.itemCount-1)
        }

        toReference.setValue(chatMessage)

        val latestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
        latestMessageRef.setValue(chatMessage)

        val latestMessageToRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")
        latestMessageToRef.setValue(chatMessage)

    }

    private fun listenForMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val toId = user.uid
        val ref = FirebaseDatabase.getInstance().getReference("/userMessages/$fromId/$toId")

        ref.addChildEventListener(object: ChildEventListener {

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)

                if (chatMessage != null) {
                    Log.d("ChatLogActivity", chatMessage.text)

                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
                        adapter.add(ChatToItem(chatMessage.text, chatMessage.timeStamp))
                    } else {
                        adapter.add(ChatFromItem(chatMessage.text, chatMessage.timeStamp))
                    }
                }
                recycleViewChatLog.scrollToPosition(adapter.itemCount-1)
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

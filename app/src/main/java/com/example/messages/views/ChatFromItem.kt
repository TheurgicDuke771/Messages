package com.example.messages.views

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.messages.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_from_row.view.*
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class ChatFromItem(private val text: String, private val milliseconds: Long): Item<ViewHolder>() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textViewChatFrom.text = text
        val timeStamp = LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneOffset.UTC)
        viewHolder.itemView.textViewTimeChatFrom.text = timeStamp.toString()
    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}

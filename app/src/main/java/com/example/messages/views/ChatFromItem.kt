package com.example.messages.views

import com.example.messages.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_from_row.view.*

class ChatFromItem(private val text: String): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textViewChatFrom.text = text
    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}

package com.example.messages.views

import com.example.messages.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatToItem(private val text: String): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textViewChatTo.text = text
    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}

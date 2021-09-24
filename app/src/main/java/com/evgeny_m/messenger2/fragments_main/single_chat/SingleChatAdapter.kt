package com.evgeny_m.messenger2.fragments_main.single_chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.evgeny_m.messenger2.R
import com.evgeny_m.messenger2.models.CommonModel
import com.evgeny_m.messenger2.utilits.CURRENT_UID
import com.evgeny_m.messenger2.utilits.asTime
import java.text.SimpleDateFormat
import java.util.*

class SingleChatAdapter: RecyclerView.Adapter<SingleChatAdapter.SingleChatHolder>() {

    private var listMassagesCache = emptyList<CommonModel>()

    class SingleChatHolder(view: View) : RecyclerView.ViewHolder(view){
        val blockUserMessage : ConstraintLayout = view.findViewById(R.id.chat_user_messege)
        val textUserMassage = view.findViewById<TextView>(R.id.chat_text_user_message)
        val timeUserMessage = view.findViewById<TextView>(R.id.chat_time_user_massage)

        val blockReceivedMassage :ConstraintLayout = view.findViewById(R.id.chat_received_message)
        val textReceivedMassage = view.findViewById<TextView>(R.id.chat_text_received_message)
        val timeReceivedMassage = view.findViewById<TextView>(R.id.chat_time_received_message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleChatHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.messege_item,parent,false)
        return SingleChatHolder(view)
    }

    override fun onBindViewHolder(holder: SingleChatHolder, position: Int) {
        if (listMassagesCache[position].from == CURRENT_UID){
            holder.blockUserMessage.visibility = View.VISIBLE
            holder.blockReceivedMassage.visibility = View.GONE
            holder.textUserMassage.text = listMassagesCache[position].text
            holder.timeUserMessage.text = listMassagesCache[position].timeStamp.toString().asTime()
        } else {
            holder.blockUserMessage.visibility = View.GONE
            holder.blockReceivedMassage.visibility = View.VISIBLE
            holder.textReceivedMassage.text = listMassagesCache[position].text
            holder.timeReceivedMassage.text = listMassagesCache[position].timeStamp.toString().asTime()
        }
    }

    override fun getItemCount(): Int {
        return listMassagesCache.size
    }

    fun setList(list : List<CommonModel>) {
        listMassagesCache = list
        notifyDataSetChanged()
    }
}

/*
private fun String.asTime(): String {
    val time = Date(this.toLong())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(time)
}
*/

package com.jspstudio.community.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jspstudio.community.databinding.ListChatMeBinding
import com.jspstudio.community.databinding.ListChatOtherBinding
import com.jspstudio.community.databinding.ListMessageBinding
import com.jspstudio.community.model.AccompanyData
import com.jspstudio.community.model.MessageData
import com.jspstudio.community.util.UtilAnim

class ChatAdapter(
    private val context: Context,
    private val id: String,
    private val onItemClick : (item: MessageData) -> Unit,
) : ListAdapter<MessageData, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    private val getId = id

    companion object {
        private const val VIEW_TYPE_ME = 1
        private const val VIEW_TYPE_OTHER = 2

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MessageData>() {
            override fun areItemsTheSame(
                oldItem: MessageData,
                newItem: MessageData
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: MessageData,
                newItem: MessageData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class MeViewHolder(private val binding: ListChatMeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ClickableViewAccessibility")
        fun bind(item: MessageData, position: Int) {
            binding.item = item
            binding.executePendingBindings()
        }
    }

    inner class OtherViewHolder(private val binding: ListChatOtherBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ClickableViewAccessibility")
        fun bind(item: MessageData, position: Int) {
            binding.item = item
            binding.executePendingBindings()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            VIEW_TYPE_ME -> {
                val binding = ListChatMeBinding.inflate(inflater, parent, false)
                MeViewHolder(binding)
            }
            VIEW_TYPE_OTHER -> {
                val binding = ListChatOtherBinding.inflate(inflater, parent, false)
                OtherViewHolder(binding)
            }
            else -> throw java.lang.IllegalArgumentException("invalid viewtype")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position) as MessageData
        when(holder) {
            is MeViewHolder -> holder.bind(item, position)
            is OtherViewHolder -> holder.bind(item, position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getId == getItem(position).id) VIEW_TYPE_ME else VIEW_TYPE_OTHER
    }
}
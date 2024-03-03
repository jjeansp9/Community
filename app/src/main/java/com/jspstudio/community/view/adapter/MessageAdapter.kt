package com.jspstudio.community.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jspstudio.community.R
import com.jspstudio.community.databinding.ListMessageBinding
import com.jspstudio.community.model.AccompanyData
import com.jspstudio.community.model.MessageData
import com.jspstudio.community.util.LogMgr
import com.jspstudio.community.util.UtilAnim

class MessageAdapter(
    private val context: Context,
    private val onItemClick : (item: MessageData) -> Unit,
) : ListAdapter<MessageData, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MessageData>() {
            override fun areItemsTheSame(
                oldItem: MessageData,
                newItem: MessageData
            ): Boolean {
                return oldItem.user.id == newItem.user.id
            }

            override fun areContentsTheSame(
                oldItem: MessageData,
                newItem: MessageData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class HomeViewHolder(private val binding: ListMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ClickableViewAccessibility")
        fun bind(item: MessageData, position: Int) {
            binding.item = item

//            binding.root.setOnTouchListener {v, event ->
//                when (event.action) {
//                    MotionEvent.ACTION_DOWN -> {
//                        UtilAnim.btnClickEffect(v, 50, 0.97f, 0.97f, true)
//                        true
//                    }
//                    MotionEvent.ACTION_CANCEL -> {
//                        UtilAnim.btnClickEffect(v,  100, 1f, 1f, false)
//                        true
//                    }
//                    MotionEvent.ACTION_UP -> {
//                        UtilAnim.btnClickEffect(v, 100, 1f, 1f, false)
//                        if (binding.item != null) onItemClick(binding.item!!)
//                        true
//                    }
//                    else -> {
//                        false
//                    }
//                }
//            }

            binding.root.setOnClickListener { if (binding.item != null) onItemClick(binding.item!!) }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListMessageBinding.inflate(inflater, parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position) as MessageData
        (holder as HomeViewHolder).bind(item, position)
    }
}
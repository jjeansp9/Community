package com.jspstudio.community.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jspstudio.community.databinding.ListImageBinding
import com.jspstudio.community.model.AccompanyData
import com.jspstudio.community.model.ImageData
import com.jspstudio.community.util.LogMgr

class GalleryAdapter(
    private val context: Context,
    private val list: MutableList<ImageData>,
    private val onItemClick : (item: ImageData) -> Unit,
) : ListAdapter<ImageData, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    private val mList = list
    private val mContext = context

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ImageData>() {
            override fun areItemsTheSame(
                oldItem: ImageData,
                newItem: ImageData
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ImageData,
                newItem: ImageData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class HomeViewHolder(private val binding: ListImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ClickableViewAccessibility")
        fun bind(item: ImageData, position: Int) {
            LogMgr.e("image", "$position ," + item + ", ${item.mimeType}")

            if (item.mimeType.contains("gif")) Glide.with(mContext).asGif().load(item.uri).into(binding.img)
            else Glide.with(mContext).load(item.uri).into(binding.img)

            binding.img.setOnClickListener { if (item != null) onItemClick(item) }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListImageBinding.inflate(inflater, parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position) as ImageData
        (holder as HomeViewHolder).bind(item, position)
    }
}
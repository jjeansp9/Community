package com.jspstudio.community.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jspstudio.community.databinding.ListImageBinding
import com.jspstudio.community.model.ImageData
import com.jspstudio.community.util.LogMgr
import java.util.Locale
import java.util.concurrent.TimeUnit

class GalleryAdapter(
    private val context: Context,
    private val onItemClick : (item: ImageData, position: Int) -> Unit,
    private val onDetailClick : (item: ImageData, position: Int) -> Unit,
) : ListAdapter<ImageData, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    private val mContext = context

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ImageData>() {
            override fun areItemsTheSame(
                oldItem: ImageData,
                newItem: ImageData
            ): Boolean {
                return oldItem.name == newItem.name
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
            binding.item = item
            LogMgr.e("image", "$position ," + item + ", ${item.mimeType}")
            if (item.mimeType.contains("gif")) {
                Glide.with(mContext).asGif().load(item.uri).into(binding.img)
                binding.tvDuration.text = "GIF"
                binding.tvDuration.visibility = View.VISIBLE
            }
            else if (item.mimeType.contains("video")) {
                Glide.with(mContext).load(item.uri).into(binding.img)

                val hours = TimeUnit.MILLISECONDS.toHours(item.duration.toLong())
                val minutes = TimeUnit.MILLISECONDS.toMinutes(item.duration.toLong()) % 60
                val seconds = TimeUnit.MILLISECONDS.toSeconds(item.duration.toLong()) % 60

                val getTime = if (hours > 0) String.format(
                    Locale.getDefault(),
                    "%01d:%02d:%02d",
                    hours,
                    minutes,
                    seconds
                ) else String.format(
                    Locale.getDefault(), "%01d:%02d", minutes, seconds
                )
                binding.tvDuration.text = getTime
                binding.tvDuration.visibility = View.VISIBLE
            }
            else {
                Glide.with(mContext).load(item.uri).into(binding.img)
                binding.tvDuration.visibility = View.GONE
            }
            binding.img.setOnClickListener {
                if (item != null) {
                    item.isCheck = !item.isCheck
                    binding.item = item
                    onItemClick(item, position)
                }
            }
            binding.lyDetail.setOnClickListener { if (item != null) onDetailClick(item, position) }
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
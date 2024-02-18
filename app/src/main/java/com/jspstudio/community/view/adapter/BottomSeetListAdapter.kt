package com.jspstudio.community.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jspstudio.community.R

class BottomSeetListAdapter(private val context: Context, private val mList: List<String>, private val onClick: (String) -> Unit) : RecyclerView.Adapter<BottomSeetListAdapter.YearViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YearViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return YearViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: YearViewHolder, position: Int) {
        holder.bind(mList[position], context)
    }

    override fun getItemCount() = mList.size

    class YearViewHolder(itemView: View, private val onClick: (String) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val tv: TextView = itemView.findViewById(android.R.id.text1)

        fun bind(year: String, context: Context) {
            tv.text = year
            tv.setTextColor(context.getColor(R.color.font_color_default))
            itemView.setBackgroundResource(R.drawable.selector_list)
            itemView.setOnClickListener {
                onClick(year)
            }
        }
    }
}
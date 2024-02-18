package com.jspstudio.community.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class YearAdapter(private val years: List<String>, private val onClick: (String) -> Unit) : RecyclerView.Adapter<YearAdapter.YearViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YearViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return YearViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: YearViewHolder, position: Int) {
        holder.bind(years[position])
    }

    override fun getItemCount() = years.size

    class YearViewHolder(itemView: View, private val onClick: (String) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val yearTextView: TextView = itemView.findViewById(android.R.id.text1)

        fun bind(year: String) {
            yearTextView.text = year
            itemView.setOnClickListener {
                onClick(year)
            }
        }
    }
}
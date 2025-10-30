package com.example.agenciadeviajes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RegionsAdapter(
    private val regions: List<String>,
    private val onRegionClick: (String) -> Unit
) : RecyclerView.Adapter<RegionsAdapter.RegionViewHolder>() {

    class RegionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewRegionName: TextView = itemView.findViewById(com.example.agenciadeviajes.R.id.textViewRegionName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(com.example.agenciadeviajes.R.layout.item_region, parent, false)
        return RegionViewHolder(view)
    }

    override fun onBindViewHolder(holder: RegionViewHolder, position: Int) {
        val region = regions[position]
        holder.textViewRegionName.text = region
        holder.itemView.setOnClickListener {
            onRegionClick(region)
        }
    }

    override fun getItemCount(): Int = regions.size
}
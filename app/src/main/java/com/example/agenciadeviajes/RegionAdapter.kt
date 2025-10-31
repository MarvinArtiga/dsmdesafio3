package com.example.agenciadeviajes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RegionAdapter(
    private val regions: List<String>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<RegionAdapter.RegionViewHolder>() {

    inner class RegionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewRegion: TextView = itemView.findViewById(R.id.textViewRegion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_region, parent, false)
        return RegionViewHolder(view)
    }

    override fun onBindViewHolder(holder: RegionViewHolder, position: Int) {
        val region = regions[position]
        holder.textViewRegion.text = region
        holder.itemView.setOnClickListener {
            onClick(region)
        }
    }

    override fun getItemCount(): Int = regions.size
}

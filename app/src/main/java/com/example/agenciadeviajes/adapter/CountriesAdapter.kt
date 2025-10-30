package com.example.agenciadeviajes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.agenciadeviajes.R
import com.example.agenciadeviajes.data.model.Country

class CountriesAdapter(
    private val countries: List<Country>,
    private val onCountryClick: (Country) -> Unit
) : RecyclerView.Adapter<CountriesAdapter.CountryViewHolder>() {

    class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewFlag: ImageView = itemView.findViewById(R.id.imageViewFlag)
        val textViewCountryName: TextView = itemView.findViewById(R.id.textViewCountryName)
        val textViewCapital: TextView = itemView.findViewById(R.id.textViewCapital)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_country, parent, false)
        return CountryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = countries[position]

        holder.textViewCountryName.text = country.name.common
        holder.textViewCapital.text = country.capital?.firstOrNull() ?: "Sin capital"

        // Cargar bandera con Glide
        Glide.with(holder.itemView.context)
            .load(country.flags.png)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
            .into(holder.imageViewFlag)

        holder.itemView.setOnClickListener {
            onCountryClick(country)
        }
    }

    override fun getItemCount(): Int = countries.size
}
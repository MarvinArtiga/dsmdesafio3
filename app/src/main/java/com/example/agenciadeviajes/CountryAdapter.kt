package com.example.agenciadeviajes


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CountryAdapter(
    private val countries: List<Country>,
    private val onClick: (Country) -> Unit
) : RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    inner class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewFlag: ImageView = itemView.findViewById(R.id.imageViewFlag)
        val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        val textViewCapital: TextView = itemView.findViewById(R.id.textViewCapital)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_country, parent, false)
        return CountryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = countries[position]
        holder.textViewName.text = country.nameText
        holder.textViewCapital.text = "Capital: ${country.capitalName}"
        Glide.with(holder.itemView.context)
            .load(country.flagUrl)
            .into(holder.imageViewFlag)


        holder.itemView.setOnClickListener {
            onClick(country)
        }
    }

    override fun getItemCount(): Int = countries.size
}

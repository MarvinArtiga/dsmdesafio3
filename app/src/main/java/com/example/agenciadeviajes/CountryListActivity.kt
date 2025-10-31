package com.example.agenciadeviajes

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CountryListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CountryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_list)

        val region = intent.getStringExtra("region") ?: return
        recyclerView = findViewById(R.id.recyclerViewCountries)
        recyclerView.layoutManager = LinearLayoutManager(this)

        CountryController.getCountriesByRegion(region) { countries: List<Country> ->
            adapter = CountryAdapter(countries) { country ->
                val intent = Intent(this, CountryDetailActivity::class.java)
                intent.putExtra("country", country)
                startActivity(intent)
            }
            recyclerView.adapter = adapter
        }
    }
}
package com.example.agenciadeviajes

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RegionActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private val regionList = listOf("Africa", "America", "Asia", "Europe", "Oceania")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_region)

        recyclerView = findViewById(R.id.recyclerViewRegions)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RegionAdapter(regionList) { selectedRegion ->
            val intent = Intent(this, CountryListActivity::class.java)
            intent.putExtra("region", selectedRegion)
            startActivity(intent)
        }
    }
}

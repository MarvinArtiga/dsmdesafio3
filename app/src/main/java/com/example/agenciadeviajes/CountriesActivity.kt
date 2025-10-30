package com.example.agenciadeviajes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agenciadeviajes.adapter.CountriesAdapter
import com.example.agenciadeviajes.controller.CountryController
import com.example.agenciadeviajes.data.model.Country
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CountriesActivity : AppCompatActivity() {

    private lateinit var recyclerViewCountries: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var textViewError: TextView
    private lateinit var textViewRegionTitle: TextView

    private val countryController = CountryController()
    private var currentRegion: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_countries)

        currentRegion = intent.getStringExtra("REGION") ?: ""
        initViews()
        loadCountriesByRegion()
    }

    private fun initViews() {
        recyclerViewCountries = findViewById(R.id.recyclerViewCountries)
        progressBar = findViewById(R.id.progressBar)
        textViewError = findViewById(R.id.textViewError)
        textViewRegionTitle = findViewById(R.id.textViewRegionTitle)

        recyclerViewCountries.layoutManager = LinearLayoutManager(this)
        textViewRegionTitle.text = "Países de $currentRegion"
    }

    private fun loadCountriesByRegion() {
        showLoading(true)

        CoroutineScope(Dispatchers.IO).launch {
            val result = countryController.getCountriesByRegion(currentRegion)

            withContext(Dispatchers.Main) {
                showLoading(false)

                when {
                    result.isSuccess -> {
                        val countries = result.getOrNull() ?: emptyList()
                        if (countries.isNotEmpty()) {
                            showCountries(countries)
                        } else {
                            showError("No se encontraron países para esta región")
                        }
                    }
                    else -> {
                        showError("Error: ${result.exceptionOrNull()?.message}")
                    }
                }
            }
        }
    }

    private fun showCountries(countries: List<Country>) {
        val adapter = CountriesAdapter(countries) { country ->
            // Navegar a detalle del país
            val intent = Intent(this, CountryDetailActivity::class.java)
            intent.putExtra("COUNTRY", country)
            startActivity(intent)
        }

        recyclerViewCountries.adapter = adapter
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        recyclerViewCountries.visibility = if (show) View.GONE else View.VISIBLE
        textViewError.visibility = View.GONE
    }

    private fun showError(message: String) {
        textViewError.text = message
        textViewError.visibility = View.VISIBLE
        recyclerViewCountries.visibility = View.GONE
        progressBar.visibility = View.GONE
    }
}
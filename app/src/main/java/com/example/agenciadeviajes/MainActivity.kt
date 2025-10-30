package com.example.agenciadeviajes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agenciadeviajes.adapter.RegionsAdapter
import com.example.agenciadeviajes.controller.CountryController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerViewRegions: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var textViewError: TextView

    private val countryController = CountryController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        loadRegions()
    }

    private fun initViews() {
        recyclerViewRegions = findViewById(R.id.recyclerViewRegions)
        progressBar = findViewById(R.id.progressBar)
        textViewError = findViewById(R.id.textViewError)

        recyclerViewRegions.layoutManager = LinearLayoutManager(this)
    }

    private fun loadRegions() {
        showLoading(true)

        val regions = countryController.getRegions()
        val adapter = RegionsAdapter(regions) { region ->
            // Navegar a la actividad de países
            val intent = Intent(this, CountriesActivity::class.java)
            intent.putExtra("REGION", region)
            startActivity(intent)
        }

        recyclerViewRegions.adapter = adapter
        showLoading(false)
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        recyclerViewRegions.visibility = if (show) View.GONE else View.VISIBLE
        textViewError.visibility = View.GONE
    }

    private fun showError(message: String) {
        textViewError.text = message
        textViewError.visibility = View.VISIBLE
        recyclerViewRegions.visibility = View.GONE
        progressBar.visibility = View.GONE
    }
}
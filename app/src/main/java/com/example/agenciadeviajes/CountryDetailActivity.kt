package com.example.agenciadeviajes

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.example.agenciadeviajes.controller.CountryController
import com.example.agenciadeviajes.data.model.Country
import com.example.agenciadeviajes.data.model.WeatherResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CountryDetailActivity : AppCompatActivity() {

    private lateinit var imageViewFlag: ImageView
    private lateinit var textViewCountryName: TextView
    private lateinit var textViewCapital: TextView
    private lateinit var textViewRegion: TextView
    private lateinit var textViewPopulation: TextView
    private lateinit var textViewLanguages: TextView
    private lateinit var textViewCurrencies: TextView

    private lateinit var cardViewWeather: CardView
    private lateinit var textViewWeatherLocation: TextView
    private lateinit var textViewTemperature: TextView
    private lateinit var textViewCondition: TextView
    private lateinit var textViewWind: TextView
    private lateinit var textViewHumidity: TextView
    private lateinit var textViewFeelsLike: TextView
    private lateinit var progressBarWeather: ProgressBar
    private lateinit var textViewWeatherError: TextView

    private val countryController = CountryController()
    private lateinit var country: Country

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_detail)

        country = intent.getSerializableExtra("COUNTRY") as Country
        initViews()
        loadCountryDetails()
        loadWeather()
    }

    private fun initViews() {
        imageViewFlag = findViewById(R.id.imageViewFlag)
        textViewCountryName = findViewById(R.id.textViewCountryName)
        textViewCapital = findViewById(R.id.textViewCapital)
        textViewRegion = findViewById(R.id.textViewRegion)
        textViewPopulation = findViewById(R.id.textViewPopulation)
        textViewLanguages = findViewById(R.id.textViewLanguages)
        textViewCurrencies = findViewById(R.id.textViewCurrencies)

        cardViewWeather = findViewById(R.id.cardViewWeather)
        textViewWeatherLocation = findViewById(R.id.textViewWeatherLocation)
        textViewTemperature = findViewById(R.id.textViewTemperature)
        textViewCondition = findViewById(R.id.textViewCondition)
        textViewWind = findViewById(R.id.textViewWind)
        textViewHumidity = findViewById(R.id.textViewHumidity)
        textViewFeelsLike = findViewById(R.id.textViewFeelsLike)
        progressBarWeather = findViewById(R.id.progressBarWeather)
        textViewWeatherError = findViewById(R.id.textViewWeatherError)
    }

    private fun loadCountryDetails() {
        // Cargar bandera
        Glide.with(this)
            .load(country.flags.png)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
            .into(imageViewFlag)

        // Información del país
        textViewCountryName.text = "${country.name.common} (${country.name.official})"
        textViewCapital.text = "Capital: ${country.capital?.firstOrNull() ?: "No disponible"}"
        textViewRegion.text = "Región: ${country.region} - ${country.subregion ?: ""}"
        textViewPopulation.text = "Población: ${formatPopulation(country.population)}"

        // Idiomas
        val languages = country.languages?.values?.joinToString(", ") ?: "No disponible"
        textViewLanguages.text = "Idiomas: $languages"

        // Monedas
        val currencies = country.currencies?.values?.joinToString(", ") {
            "${it.name} (${it.symbol ?: ""})"
        } ?: "No disponible"
        textViewCurrencies.text = "Monedas: $currencies"
    }

    private fun loadWeather() {
        val capital = country.capital?.firstOrNull()
        if (capital.isNullOrEmpty()) {
            showWeatherError("No hay información de capital para obtener el clima")
            return
        }

        showWeatherLoading(true)

        CoroutineScope(Dispatchers.IO).launch {
            val result = countryController.getWeatherByCapital(capital)

            withContext(Dispatchers.Main) {
                showWeatherLoading(false)

                when {
                    result.isSuccess -> {
                        val weather = result.getOrNull()
                        if (weather != null) {
                            showWeatherInfo(weather)
                        } else {
                            showWeatherError("No se pudo obtener información del clima")
                        }
                    }
                    else -> {
                        showWeatherError("Error del clima: ${result.exceptionOrNull()?.message}")
                    }
                }
            }
        }
    }

    private fun showWeatherInfo(weather: WeatherResponse) {
        cardViewWeather.visibility = View.VISIBLE
        textViewWeatherError.visibility = View.GONE

        textViewWeatherLocation.text = "${weather.location.name}, ${weather.location.country}"
        textViewTemperature.text = "${weather.current.tempC}°C"
        textViewCondition.text = weather.current.condition.text
        textViewWind.text = "Viento: ${weather.current.windKph} kph"
        textViewHumidity.text = "Humedad: ${weather.current.humidity}%"
        textViewFeelsLike.text = "Sensación térmica: ${weather.current.feelsLikeC}°C"
    }

    private fun showWeatherLoading(show: Boolean) {
        progressBarWeather.visibility = if (show) View.VISIBLE else View.GONE
        if (show) {
            cardViewWeather.visibility = View.GONE
            textViewWeatherError.visibility = View.GONE
        }
    }

    private fun showWeatherError(message: String) {
        textViewWeatherError.text = message
        textViewWeatherError.visibility = View.VISIBLE
        cardViewWeather.visibility = View.GONE
        progressBarWeather.visibility = View.GONE
    }

    private fun formatPopulation(population: Int): String {
        return when {
            population >= 1_000_000 -> "${population / 1_000_000}M"
            population >= 1_000 -> "${population / 1_000}K"
            else -> population.toString()
        }
    }
}
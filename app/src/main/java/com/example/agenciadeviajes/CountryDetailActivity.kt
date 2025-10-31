package com.example.agenciadeviajes

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
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

    // Nuevas variables para la búsqueda y icono de clima
    private lateinit var searchEditText: EditText
    private lateinit var searchErrorLayout: LinearLayout
    private lateinit var weatherIconImageView: ImageView

    private val countryController = CountryController()
    private lateinit var country: Country

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_detail)

        Log.d("CountryDetail", "✅ Actividad creada")

        try {
            // Verificar que tenemos el extra
            if (!intent.hasExtra("COUNTRY")) {
                Log.e("CountryDetail", "❌ No hay datos del país")
                Toast.makeText(this, "Error: No hay datos del país", Toast.LENGTH_LONG).show()
                finish()
                return
            }

            country = intent.getSerializableExtra("COUNTRY") as Country
            Log.d("CountryDetail", "✅ País recibido: ${country.name.common}")
            Log.d("CountryDetail", "✅ Capital: ${country.capital?.firstOrNull()}")

            initViews()
            initSearch() // <- AGREGAR ESTA LINEA
            loadCountryDetails()
            loadWeather()

        } catch (e: Exception) {
            Log.e("CountryDetail", "❌ ERROR CRÍTICO: ${e.message}")
            e.printStackTrace()
            Toast.makeText(this, "Error crítico: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
        }
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

        // Nuevas vistas del diseño
        weatherIconImageView = findViewById(R.id.weatherIconImageView)
    }

    private fun initSearch() {
        searchEditText = findViewById(R.id.searchEditText)
        searchErrorLayout = findViewById(R.id.searchErrorLayout)

        searchEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = searchEditText.text.toString().trim()
                if (query.isNotEmpty()) {
                    // Simulación de búsqueda - aquí integrarías tu lógica real
                    if (query.equals(country.name.common, ignoreCase = true)) {
                        searchErrorLayout.visibility = View.GONE
                        Toast.makeText(this, "Mostrando información de $query", Toast.LENGTH_SHORT).show()
                    } else {
                        searchErrorLayout.visibility = View.VISIBLE
                    }

                    // Ocultar teclado
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)
                }
                true
            } else {
                false
            }
        }
    }

    private fun loadCountryDetails() {
        Log.d("CountryDetail", "🔄 Cargando detalles del país...")

        try {
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

            Log.d("CountryDetail", "✅ Detalles cargados exitosamente")
        } catch (e: Exception) {
            Log.e("CountryDetail", "❌ Error en loadCountryDetails: ${e.message}")
            e.printStackTrace()
            Toast.makeText(this, "Error cargando datos del país", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadWeather() {
        val capital = country.capital?.firstOrNull()
        if (capital.isNullOrEmpty()) {
            Log.e("CountryDetail", "❌ No hay capital disponible")
            textViewWeatherError.text = "No hay información de capital"
            textViewWeatherError.visibility = View.VISIBLE
            cardViewWeather.visibility = View.GONE
            progressBarWeather.visibility = View.GONE
            return
        }

        Log.d("CountryDetail", "🌤️ Solicitando clima para: $capital")

        // Mostrar loading
        progressBarWeather.visibility = View.VISIBLE
        cardViewWeather.visibility = View.GONE
        textViewWeatherError.visibility = View.GONE

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = countryController.getWeatherByCapital(capital)

                withContext(Dispatchers.Main) {
                    // Ocultar loading
                    progressBarWeather.visibility = View.GONE

                    when {
                        result.isSuccess -> {
                            val weather = result.getOrNull()
                            if (weather != null) {
                                Log.d("CountryDetail", "✅ Clima recibido: ${weather.current.tempC}°C")
                                showWeatherInfo(weather)
                            } else {
                                Log.e("CountryDetail", "❌ Clima nulo")
                                showWeatherError("No se pudo obtener información del clima")
                            }
                        }
                        else -> {
                            val error = result.exceptionOrNull()?.message ?: "Error desconocido"
                            Log.e("CountryDetail", "❌ Error clima: $error")
                            showWeatherError("Error: $error")
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    progressBarWeather.visibility = View.GONE
                    Log.e("CountryDetail", "💥 Excepción en clima: ${e.message}")
                    showWeatherError("Error de conexión: ${e.message}")
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
        textViewWind.text = "${weather.current.windKph} kph"
        textViewHumidity.text = "${weather.current.humidity}%"
        textViewFeelsLike.text = "${weather.current.feelsLikeC}°C"

        // Cambiar icono según condición climática
        val condition = weather.current.condition.text.toLowerCase()
        weatherIconImageView.setImageResource(
            when {
                condition.contains("rain") || condition.contains("lluvia") -> R.drawable.ic_weather_rainy
                condition.contains("sun") || condition.contains("soleado") -> R.drawable.ic_weather_sunny
                condition.contains("cloud") || condition.contains("nublado") -> R.drawable.ic_weather_cloudy
                else -> R.drawable.ic_weather_sunny
            }
        )
    }

    private fun showWeatherError(message: String) {
        textViewWeatherError.text = message
        textViewWeatherError.visibility = View.VISIBLE
        cardViewWeather.visibility = View.GONE
    }

    private fun formatPopulation(population: Int): String {
        return when {
            population >= 1_000_000 -> "${population / 1_000_000}M"
            population >= 1_000 -> "${population / 1_000}K"
            else -> population.toString()
        }
    }
}
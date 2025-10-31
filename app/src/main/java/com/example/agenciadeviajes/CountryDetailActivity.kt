package com.example.agenciadeviajes
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class CountryDetailActivity : AppCompatActivity() {
    private lateinit var imageViewFlag: ImageView
    private lateinit var textViewName: TextView
    private lateinit var textViewCapital: TextView
    private lateinit var textViewWeather: TextView
    private lateinit var imageViewWeatherIcon: ImageView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_detail)

        val country = intent.getParcelableExtra<Country>("country") ?: return

        imageViewFlag = findViewById(R.id.imageViewFlag)
        textViewName = findViewById(R.id.textViewName)
        textViewCapital = findViewById(R.id.textViewCapital)
        textViewWeather = findViewById(R.id.textViewWeather)
        imageViewWeatherIcon = findViewById(R.id.imageViewWeatherIcon)
        progressBar = findViewById(R.id.progressBar)

        textViewName.text = country.name
        textViewCapital.text = "Capital: ${country.capital}"
        Glide.with(this).load(country.flagUrl).into(imageViewFlag)

        progressBar.visibility = View.VISIBLE
        WeatherController.getWeatherByCity(country.capital) { weather: Weather?, error: String? ->
            progressBar.visibility = View.GONE
            if (weather != null) {
                textViewWeather.text = "${weather.temp}°C - ${weather.condition}"
                Glide.with(this).load(weather.iconUrl).into(imageViewWeatherIcon)
            } else {
                textViewWeather.text = "Clima no disponible: $error"
            }
        }
    }
}
package com.example.agenciadeviajes.controller

import android.util.Log
import com.example.agenciadeviajes.data.api.ApiClient
import com.example.agenciadeviajes.data.model.Country
import com.example.agenciadeviajes.data.model.WeatherResponse

class CountryController {

    private val countriesApi = ApiClient.countriesApi
    private val weatherApi = ApiClient.weatherApi

    suspend fun getAllCountries(): Result<List<Country>> {
        Log.d("CountryController", "🌍 Solicitando todos los países")
        return try {
            val response = countriesApi.getAllCountries()
            Log.d("CountryController", "📡 Respuesta países: ${response.code()} - ${response.isSuccessful}")

            if (response.isSuccessful && response.body() != null) {
                val countries = response.body()!!
                Log.d("CountryController", "✅ Países obtenidos: ${countries.size}")
                Result.success(countries)
            } else {
                Log.e("CountryController", "❌ Error países: ${response.code()}")
                Result.failure(Exception("Error al obtener países: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("CountryController", "💥 Excepción países: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun getCountriesByRegion(region: String): Result<List<Country>> {
        Log.d("CountryController", "🌍 Solicitando países de región: $region")
        return try {
            val response = countriesApi.getCountriesByRegion(region)
            Log.d("CountryController", "📡 Respuesta región: ${response.code()} - ${response.isSuccessful}")

            if (response.isSuccessful && response.body() != null) {
                val countries = response.body()!!
                Log.d("CountryController", "✅ Países de región obtenidos: ${countries.size}")
                Result.success(countries)
            } else {
                Log.e("CountryController", "❌ Error región: ${response.code()}")
                Result.failure(Exception("Error al obtener países de la región: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("CountryController", "💥 Excepción región: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun getWeatherByCapital(capital: String): Result<WeatherResponse> {
        Log.d("CountryController", "🌤️ Solicitando clima para: $capital")
        return try {
            val response = weatherApi.getCurrentWeather(query = capital)
            Log.d("CountryController", "📡 Respuesta clima: ${response.code()} - ${response.isSuccessful}")

            if (response.isSuccessful && response.body() != null) {
                val weather = response.body()!!
                Log.d("CountryController", "✅ Clima obtenido: ${weather.current.tempC}°C")
                Result.success(weather)
            } else {
                val errorMsg = "Error API clima: ${response.code()} - ${response.message()}"
                Log.e("CountryController", "❌ $errorMsg")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e("CountryController", "💥 Excepción clima: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }

    fun getRegions(): List<String> {
        return listOf("Africa", "Americas", "Asia", "Europe", "Oceania")
    }
}
package com.example.agenciadeviajes.controller

import com.example.agenciadeviajes.data.api.ApiClient
import com.example.agenciadeviajes.data.model.Country
import com.example.agenciadeviajes.data.model.WeatherResponse

class CountryController {

    private val countriesApi = ApiClient.countriesApi
    private val weatherApi = ApiClient.weatherApi

    suspend fun getAllCountries(): Result<List<Country>> {
        return try {
            val response = countriesApi.getAllCountries()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener países: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCountriesByRegion(region: String): Result<List<Country>> {
        return try {
            val response = countriesApi.getCountriesByRegion(region)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener países de la región: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getWeatherByCapital(capital: String): Result<WeatherResponse> {
        return try {
            val response = weatherApi.getCurrentWeather(query = capital)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener clima: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getRegions(): List<String> {
        return listOf("Africa", "Americas", "Asia", "Europe", "Oceania")
    }
}
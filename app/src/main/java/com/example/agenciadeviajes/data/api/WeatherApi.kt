package com.example.agenciadeviajes.data.api

import com.example.agenciadeviajes.BuildConfig
import com.example.agenciadeviajes.data.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String = BuildConfig.WEATHER_API_KEY,
        @Query("q") query: String
    ): Response<WeatherResponse>
}
package com.example.agenciadeviajes.data.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("location") val location: Location,
    @SerializedName("current") val current: CurrentWeather
)

data class Location(
    @SerializedName("name") val name: String,
    @SerializedName("region") val region: String,
    @SerializedName("country") val country: String,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double
)

data class CurrentWeather(
    @SerializedName("temp_c") val tempC: Double,
    @SerializedName("temp_f") val tempF: Double,
    @SerializedName("condition") val condition: WeatherCondition,
    @SerializedName("wind_kph") val windKph: Double,
    @SerializedName("wind_mph") val windMph: Double,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("feelslike_c") val feelsLikeC: Double
)

data class WeatherCondition(
    @SerializedName("text") val text: String,
    @SerializedName("icon") val icon: String
)
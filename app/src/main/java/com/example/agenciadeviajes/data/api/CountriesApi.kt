package com.example.agenciadeviajes.data.api

import com.example.agenciadeviajes.data.model.Country
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CountriesApi {

    @GET("all")
    suspend fun getAllCountries(): Response<List<Country>>

    @GET("region/{region}")
    suspend fun getCountriesByRegion(@Path("region") region: String): Response<List<Country>>
}
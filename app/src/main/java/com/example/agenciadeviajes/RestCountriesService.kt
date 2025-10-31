package com.example.agenciadeviajes

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RestCountriesService {
    @GET("v3.1/region/{region}")
    fun getCountriesByRegion(@Path("region") region: String): Call<List<Country>>
}
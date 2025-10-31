package com.example.agenciadeviajes

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

object CountryController {

    private val apiService = RetrofitClient.restCountriesService

    fun getCountriesByRegion(region: String, onResult: (List<Country>) -> Unit) {
        val call = apiService.getCountriesByRegion(region)

        call.enqueue(object : Callback<List<Country>> {
            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                if (response.isSuccessful) {
                    val countries = response.body() ?: emptyList()
                    onResult(countries)
                } else {
                    onResult(emptyList())
                }
            }

            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                onResult(emptyList())
            }
        })
    }
}

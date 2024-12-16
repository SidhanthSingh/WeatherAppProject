package com.example.weatherapp.repository

import com.example.weatherapp.model.WeatherResponse
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherApiService: WeatherApiService
) {

    suspend fun getWeather(city: String): WeatherResponse {
        val response = weatherApiService.getWeather(city = city, apiKey = "0dacb937e8744e6fa4c191239241512")

        if (response.isSuccessful) {
            return response.body() ?: throw Exception("No data available")
        } else {
            throw Exception("Failed to fetch weather data")
        }
    }
}

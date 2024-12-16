package com.example.weatherapp

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("WeatherAppPrefs", Context.MODE_PRIVATE)

    companion object {
        const val CITY_KEY = "saved_city"
    }

    // Save the city name
    fun saveCity(city: String) {
        sharedPreferences.edit().putString(CITY_KEY, city).apply()
    }

    // Retrieve the saved city name
    fun getSavedCity(): String? {
        return sharedPreferences.getString(CITY_KEY, null)
    }
}

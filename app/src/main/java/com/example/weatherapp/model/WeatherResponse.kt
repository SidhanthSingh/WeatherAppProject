package com.example.weatherapp.model

data class WeatherResponse(
    val location: Location,
    val current: CurrentWeather
)

data class Location(
    val name: String,
    val region: String,
    val country: String
)

data class CurrentWeather(
    val temp_c: Float,
    val condition: Condition,
    val humidity: Int,
    val uv: Float,
    val feelslike_c: Float
)

data class Condition(
    val text: String,
    val icon: String
)

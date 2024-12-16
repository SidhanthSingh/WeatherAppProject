package com.example.weatherapp.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.SharedPreferencesManager
import com.example.weatherapp.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel  @Inject constructor(
    private val repository: WeatherRepository,
    private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {

    private val _weatherState = MutableStateFlow<WeatherState>(WeatherState.Default)
    val weatherState: StateFlow<WeatherState> = _weatherState

    init {
        val savedCity = sharedPreferencesManager.getSavedCity()
        if (savedCity != null) {
            fetchWeather(savedCity, showDetails = true)
        }
    }

    fun fetchWeather(city: String, showDetails: Boolean) {
        viewModelScope.launch {
            _weatherState.value = WeatherState.Loading
            try {
                val result = repository.getWeather(city = city)
                _weatherState.value = WeatherState.Success(result, showDetails)
            } catch (e: Exception) {
                _weatherState.value = WeatherState.Error(e)
            }
        }
    }

    fun toggleDetails() {
        _weatherState.value = when (val currentState = _weatherState.value) {
            is WeatherState.Success -> {
                currentState.copy(showingDetails = !currentState.showingDetails)
            }
            else -> currentState
        }
    }

    fun saveCityToPreferences(city: String) {
        sharedPreferencesManager.saveCity(city)
    }

}

sealed class WeatherState {
    object Loading : WeatherState()
    object Default : WeatherState()
    data class Success(val weatherResponse: WeatherResponse, val showingDetails: Boolean = false) : WeatherState()
    data class Error(val error: Throwable) : WeatherState()
}

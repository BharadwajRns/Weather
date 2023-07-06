package com.example.weather.ui.uiStates

import com.example.weather.db.entitys.WeatherEntity


sealed class CurrentLocationUIState {

    object Loading : CurrentLocationUIState()
    data class Success(val weatherData : WeatherEntity) : CurrentLocationUIState()
    data class Error(val message : String? = null) : CurrentLocationUIState()


}
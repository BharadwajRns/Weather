package com.example.weather.ui.uiStates

import com.example.weather.db.entitys.WeatherEntity


sealed class FamousLocationUIState {

    object Loading : FamousLocationUIState()
    data class Success(val weatherData : MutableList<WeatherEntity>) : FamousLocationUIState()
    data class Error(val message : String? = null) : FamousLocationUIState()


}
package com.example.weather.di

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import retrofit2.Retrofit
import javax.inject.Inject

@HiltAndroidApp
class WeatherApplication : Application() {
    fun getWeatherContext() : Context = this.applicationContext
}
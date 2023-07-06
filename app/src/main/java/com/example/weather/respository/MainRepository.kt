package com.example.weather.respository

import com.example.weather.api.WeatherApiCalls
import com.example.weather.db.WeatherDao
import com.example.weather.db.entitys.WeatherEntity
import retrofit2.Retrofit
import javax.inject.Inject


class MainRepository @Inject constructor(
    private val weatherDao: WeatherDao,
    private val weatherApiCalls: WeatherApiCalls
) {

    suspend fun insertWeatherDataIntoRoom(
        weatherEntity: WeatherEntity
    ) = weatherDao.insertWeatherData(weatherEntity)

    fun getWeatherDataFromRoom() = weatherDao.getWeatherData()

    suspend fun getWeatherFromApi(
        appId: String,
        lat: Double,
        lon: Double,
        exclude: String,
        units :String
    ) = weatherApiCalls.getWeatherData(
        appId,
        lat,
        lon,
        exclude,
        units
    )
}
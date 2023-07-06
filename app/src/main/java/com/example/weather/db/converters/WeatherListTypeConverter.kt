package com.example.weather.db.converters

import androidx.room.TypeConverter
import com.example.weather.db.entitys.Weather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WeatherListTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromWeatherList(weatherList: List<Weather>): String {
        return gson.toJson(weatherList)
    }

    @TypeConverter
    fun toWeatherList(weatherListJson: String): List<Weather> {
        val type = object : TypeToken<List<Weather>>() {}.type
        return gson.fromJson(weatherListJson, type)
    }
}
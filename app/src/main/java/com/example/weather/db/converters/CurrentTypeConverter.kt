package com.example.weather.db.converters

import androidx.room.TypeConverter
import com.example.weather.db.entitys.Current
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


//converters for converting data classes to json (vice versa)
class CurrentTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromCurrent(current: Current): String {
        return gson.toJson(current)
    }

    @TypeConverter
    fun toCurrent(currentJson: String): Current {
        val type = object : TypeToken<Current>() {}.type
        return gson.fromJson(currentJson, type)
    }
}
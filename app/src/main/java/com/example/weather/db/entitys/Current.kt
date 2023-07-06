package com.example.weather.db.entitys

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.weather.db.converters.WeatherListTypeConverter
import java.io.Serializable

@Entity
@TypeConverters(WeatherListTypeConverter::class)
data class Current(
    @PrimaryKey(autoGenerate = false)
    val current_id : Int? = 1,
    val clouds: Int,
    val dew_point: Double,
    val dt: Int,
    val feels_like: Double,
    val humidity: Int,
    val pressure: Int,
    val sunrise: Int,
    val sunset: Int,
    val temp: Double,
    val uvi: Double,
    val visibility: Int,
    val weather: List<Weather>,
    val wind_deg: Int,
    val wind_speed: Double
) : Serializable
package com.example.weather.db.entitys

import android.os.Parcelable
import androidx.room.*
import com.example.weather.db.converters.CurrentTypeConverter
import java.io.Serializable

@Entity(tableName = "weather_location_data")
@TypeConverters(CurrentTypeConverter::class)
data class WeatherEntity(
    @PrimaryKey(autoGenerate = false)
    var id : Int? = 1,
    val current: Current,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int
) : Serializable

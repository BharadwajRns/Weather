package com.example.weather.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weather.db.entitys.WeatherEntity

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherData(
        weatherEntity: WeatherEntity
    )

    @Query("SELECT * FROM weather_location_data")
    fun getWeatherData() : LiveData<WeatherEntity>
}
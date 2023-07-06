package com.example.weather.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weather.db.entitys.Current
import com.example.weather.db.entitys.Weather
import com.example.weather.db.entitys.WeatherEntity

@Database(
    entities = [WeatherEntity::class, Weather::class,Current::class],
    version = 1
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao() : WeatherDao
}
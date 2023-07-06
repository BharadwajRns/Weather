package com.example.weather.db.entitys

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Weather(
    @PrimaryKey(autoGenerate = false)
    val weather_id: Int? = 1,
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
) : Serializable
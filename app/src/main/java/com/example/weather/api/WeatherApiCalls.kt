package com.example.weather.api

import com.example.weather.db.entitys.WeatherEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.text.DecimalFormat

interface WeatherApiCalls {

    /*
       Api calls for fetching data
     */
    @GET("onecall")
    suspend fun getWeatherData(
        @Query("appid") appId : String,
        @Query("lat") latitude : Double,
        @Query("lon") longitude : Double,
        @Query("exclude") exclude : String,
        @Query("units") units : String
    ) : Response<WeatherEntity>
}
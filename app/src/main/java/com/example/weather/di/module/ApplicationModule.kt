package com.example.weather.di.module

import android.content.Context
import androidx.room.Room
import com.example.weather.api.WeatherApiCalls
import com.example.weather.db.WeatherDatabase
import com.example.weather.utils.Constants.URL
import com.squareup.okhttp.OkHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor
import retrofit.RxJavaCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import rx.schedulers.Schedulers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext ctx: Context) : Context {
        return ctx
    }

    @Provides
    @Singleton
    fun provideHTTPLoggingInterceptor() : HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return loggingInterceptor
    }

    @Provides
    @Singleton
    fun provideClient() : okhttp3.OkHttpClient {
        return okhttp3.OkHttpClient.Builder()
            .addInterceptor(provideHTTPLoggingInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideRoom(
        @ApplicationContext ctx : Context
    ) = Room.databaseBuilder(
        ctx,
        WeatherDatabase::class.java,
        "WeatherDatabase"
        ).build()

    @Provides
    @Singleton
    fun provideWeatherDao(db : WeatherDatabase) = db.weatherDao()

    @Provides
    @Singleton
     fun retrofit() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(URL)
            .client(provideClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(retrofit: Retrofit): WeatherApiCalls = retrofit.create(WeatherApiCalls::class.java)
}
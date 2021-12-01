package net.nov.weatherkotlin.rest

import android.telecom.Call
import net.nov.weatherkotlin.model.rest_entities.WeatherDTO

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("informers")
    fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ) : retrofit2.Call<WeatherDTO>
}

interface WeatherAPI {
    @GET("informers")
    fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ) : retrofit2.Call<WeatherDTO>
}
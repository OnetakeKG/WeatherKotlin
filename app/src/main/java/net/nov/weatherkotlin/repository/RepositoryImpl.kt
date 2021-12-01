package net.nov.weatherkotlin.repository

import net.nov.weatherkotlin.WeatherLoader
import net.nov.weatherkotlin.entities.City
import net.nov.weatherkotlin.entities.Weather
import net.nov.weatherkotlin.rest.WeatherRepo


class RepositoryImpl : Repository {
    override fun getWeatherFromServer(lat: Double, lng: Double, ): Weather {
        val dto = WeatherRepo.api.getWeather(lat, lng).execute().body()

        return Weather(
            temperature = dto?.fact?.temp ?: 0,
            feelsLike = dto?.fact?.feelsLike ?: 0,
            condition = dto?.fact?.condition
        )
    }

    override fun getWeatherFromLocalStorageRus() = City.getRussianCities()

    override fun getWeatherFromLocalStorageWorld() = City.getWorldCities()
}
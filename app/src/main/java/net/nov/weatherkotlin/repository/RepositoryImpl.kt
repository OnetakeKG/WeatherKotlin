package net.nov.weatherkotlin.repository

import net.nov.weatherkotlin.WeatherLoader
import net.nov.weatherkotlin.entities.City
import net.nov.weatherkotlin.entities.Weather


class RepositoryImpl : Repository {
    override fun getWeatherFromServer(lat: Double, lng: Double): Weather {
        val dto = WeatherLoader.loadWeather(lat, lng)
        return Weather(
            temperature = dto?.fact?.temp ?: 0,
            feelsLike = dto?.fact?.feels_like ?: 0,
            condition = dto?.fact?.condition
        )
    }

    override fun getWeatherFromLocalStorageRus() = City.getRussianCities()

    override fun getWeatherFromLocalStorageWorld() = City.getWorldCities()
}
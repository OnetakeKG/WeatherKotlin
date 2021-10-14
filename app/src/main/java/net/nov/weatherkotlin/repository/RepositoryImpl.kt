package net.nov.weatherkotlin.repository

import net.nov.weatherkotlin.entities.Weather
import net.nov.weatherkotlin.entities.getRussianCities
import net.nov.weatherkotlin.entities.getWorldCities

class RepositoryImpl : Repository {
    override fun getWeatherFromServer() = Weather()
    override fun getWeatherFromLocalStorageRus() = getRussianCities()
    override fun getWeatherFromLocalStorageWorld() = getWorldCities()
}
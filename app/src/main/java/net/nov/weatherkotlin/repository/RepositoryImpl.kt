package net.nov.weatherkotlin.repository

import net.nov.weatherkotlin.entities.City
import net.nov.weatherkotlin.entities.Weather


class RepositoryImpl : Repository {
    override fun getWeatherFromServer() = Weather()
    override fun getWeatherFromLocalStorageRus() = City.getRussianCities()
    override fun getWeatherFromLocalStorageWorld() = City.getWorldCities()
}
package net.nov.weatherkotlin.repository

import net.nov.weatherkotlin.WeatherLoader
import net.nov.weatherkotlin.database.Database
import net.nov.weatherkotlin.database.HistoryEntity
import net.nov.weatherkotlin.entities.City

import net.nov.weatherkotlin.entities.Weather
import net.nov.weatherkotlin.entities.getRussianCities
import net.nov.weatherkotlin.entities.getWorldCities
import net.nov.weatherkotlin.rest.WeatherRepo


class RepositoryImpl : Repository {
    override fun getWeatherFromServer(lat: Double, lng: Double): Weather {
        //val dto = WeatherLoader.loadWeather(lat, lng)
        val dto = WeatherRepo.api.getWeather(lat, lng).execute().body()
        return Weather(
            temperature = dto?.fact?.temp,
            feelsLike = dto?.fact?.feelsLike,
            condition = dto?.fact?.condition
        )
    }

    override fun getWeatherFromLocalStorageRus() = getRussianCities()
    override fun getWeatherFromLocalStorageWorld() = getWorldCities()

    override fun getAllHistory(): List<Weather> =
        convertHistoryEntityToWeather(Database.db.historyDao().all())


    override fun saveEntity(weather: Weather) {
        Database.db.historyDao().insert(convertWeatherToEntity(weather))
    }

    private fun convertHistoryEntityToWeather(entityList: List<HistoryEntity>): List<Weather> =
        entityList.map {
            Weather(City(it.city, 0.0, 0.0), it.temperature, 0, it.condition)
        }


    private fun convertWeatherToEntity(weather: Weather): HistoryEntity =
        HistoryEntity(0, weather.city.city,
            weather.temperature ?: 0,
            weather.condition ?: ""
        )
}
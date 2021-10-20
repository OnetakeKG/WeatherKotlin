package net.nov.weatherkotlin.model.rest_entities

data class FactDTO(
    val temp: Int?,
    val feels_like: Int?,
    val condition: String?
)
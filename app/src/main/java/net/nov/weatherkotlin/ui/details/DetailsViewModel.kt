package net.nov.weatherkotlin.ui.details

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import kotlinx.coroutines.*
import net.nov.weatherkotlin.AppState
import net.nov.weatherkotlin.entities.City
import net.nov.weatherkotlin.repository.Repository

class DetailsViewModel(private val repository: Repository)
    : ViewModel(), LifecycleObserver, CoroutineScope by MainScope() {
    val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()

    fun loadData(city: City) {
        liveDataToObserve.value = AppState.Loading
        launch {
            val job = async(Dispatchers.IO) {
                val data = repository.getWeatherFromServer(city.lat, city.lon)
                data.city = city
                repository.saveEntity(data)
                data
            }
            liveDataToObserve.value = AppState.Success(listOf(job.await()))
        }
    }
}
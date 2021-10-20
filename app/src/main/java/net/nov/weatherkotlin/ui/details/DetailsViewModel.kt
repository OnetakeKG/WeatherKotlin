package net.nov.weatherkotlin.ui.details

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.nov.weatherkotlin.AppState
import net.nov.weatherkotlin.repository.Repository

class DetailsViewModel(private val repository: Repository) : ViewModel(), LifecycleObserver {
    val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()

    fun loadData(lat: Double, lon: Double) {
        liveDataToObserve.value = AppState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val data = repository.getWeatherFromServer(lat, lon)
            liveDataToObserve.postValue(AppState.Success(listOf(data)))
        }
    }
}
package net.nov.weatherkotlin.ui

import android.util.Log
import androidx.lifecycle.*
import net.nov.weatherkotlin.AppState
import net.nov.weatherkotlin.repository.Repository

class MainViewModel(private val repository: Repository) : ViewModel(), LifecycleObserver {
    private val liveData = MutableLiveData<AppState>()

    fun getLiveData(): LiveData<AppState> = liveData

    fun getWeatherFromLocalSourceRus() = getDataFromLocalSource(true)

    fun getWeatherFromLocalSourceWorld() = getDataFromLocalSource(false)

    private fun getDataFromLocalSource(isRussian: Boolean) {
        liveData.value = AppState.Loading
        Thread {
            Thread.sleep(1000)
            liveData.postValue(
                if (isRussian) {
                    AppState.Success(repository.getWeatherFromLocalStorageRus())
                } else {
                    AppState.Success(repository.getWeatherFromLocalStorageWorld())
                }
            )
        }.start()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onViewStart() {
        Log.i("LifecycleEvent", "onStart")
    }
}
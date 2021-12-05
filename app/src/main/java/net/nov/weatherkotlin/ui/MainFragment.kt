package net.nov.weatherkotlin.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import net.nov.weatherkotlin.AppState
import net.nov.weatherkotlin.entities.Weather

import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.lifecycle.Observer
import com.example.a2kotlinwithmvvm.R
import com.example.a2kotlinwithmvvm.databinding.MainFragmentBinding
import kotlinx.coroutines.*

import net.nov.weatherkotlin.entities.City
import net.nov.weatherkotlin.experiments.services.showSnackBar
import net.nov.weatherkotlin.showSnackBar
import net.nov.weatherkotlin.ui.adapters.MainFragmentAdapter
import java.io.IOException

import java.util.*
import java.util.concurrent.TimeUnit


@Suppress("NAME_SHADOWING")
class MainFragment : Fragment(), CoroutineScope by MainScope() {
    private lateinit var binding: MainFragmentBinding
    private val viewModel: MainViewModel by viewModel()

    private var adapter: MainFragmentAdapter? = null
    private var isDataSetRus = true

    private val permissionResult = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
        if (result) {
            getLocation()
        } else {
            Toast.makeText(
                context,
                getString(R.string.dialog_message_no_gps),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private val onLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            getAddressAsync(location)
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainFragmentRecyclerView.adapter = adapter
        binding.mainFragmentFAB.setOnClickListener { changeWeatherDataSet() }
        binding.mainFragmentFABLocation.setOnClickListener { checkPermission() }
        viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
        viewModel.getWeatherFromLocalSourceRus()

        initDataSet()
        loadDataSet()

        /*Thread {
            startCalculations(10)
        }.start()

        val handlerThread = HandlerThread("tag")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        handler.post {  }*/

        /*launch {
            delay(1000)
            val job = async(Dispatchers.Default) {
                startCalculations(10)
            }
            println(job.await())
        }*/
    }

    private fun initDataSet() {
        activity?.let {
            isDataSetRus = activity
                ?.getPreferences(Context.MODE_PRIVATE)
                ?.getBoolean(dataSetKey, true) ?: true
        }
    }

    private fun changeWeatherDataSet() = with(binding) {
        isDataSetRus = !isDataSetRus
        loadDataSet()
        //val a = activity?.getSharedPreferences("ddssd", Context.MODE_PRIVATE)
    }

    private fun loadDataSet() = with(binding) {
        if (isDataSetRus) {
            viewModel.getWeatherFromLocalSourceWorld()
            mainFragmentFAB.setImageResource(R.drawable.ic_earth)
        } else {
            viewModel.getWeatherFromLocalSourceRus()
            mainFragmentFAB.setImageResource(R.drawable.ic_russia)
        }
        saveDataSetToDisk()
    }

    private fun saveDataSetToDisk() {
        val editor = activity?.getPreferences(Context.MODE_PRIVATE)?.edit()
        editor?.putBoolean(dataSetKey, isDataSetRus)
        editor?.apply()
    }

    private fun checkPermission() {
        context?.let { notNullContext ->
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(notNullContext, Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    //Доступ к местоположению на телефоне есть
                    getLocation()
                }
                else -> {
                    //Запрашиваем разрешение
                    permissionResult.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        activity?.let { context ->
            // Получить менеджер геолокаций
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                val provider = locationManager.getProvider(LocationManager.GPS_PROVIDER)
                provider?.let {
                    // Будем получать геоположение через каждые 60 секунд или каждые 100 метров
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        1000,
                        10f,
                        onLocationListener
                    )
                }
            } else {
                val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (location == null) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.looks_like_location_disabled),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    getAddressAsync(location)
                }
            }
        }
    }

    private fun getAddressAsync(location: Location) = with(binding) {
        val geoCoder = Geocoder(context)
        launch(Dispatchers.IO) {
            try {
                val addresses = geoCoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1
                )
                withContext(Dispatchers.Main) {
                    showAddressDialog(addresses[0].getAddressLine(0), location)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun showAddressDialog(address: String, location: Location) {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_address_title))
                .setMessage(address)
                .setPositiveButton(getString(R.string.dialog_address_get_weather)) { _, _ ->
                    openDetailsFragment(
                        Weather(
                            City(
                                address,
                                location.latitude,
                                location.longitude
                            )
                        )
                    )
                }
                .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    private fun openDetailsFragment(weather: Weather) {
        activity?.supportFragmentManager?.let { manager ->
            val bundle = Bundle().apply {
                putParcelable(DetailsFragment.BUNDLE_EXTRA, weather)
            }
            manager.beginTransaction()
                .add(R.id.container, DetailsFragment.newInstance(bundle))
                .addToBackStack("")
                .commitAllowingStateLoss()
        }
    }

    private fun renderData(appState: AppState) = with(binding) {
        when (appState) {
            is AppState.Success -> {
                mainFragmentLoadingLayout.postDelayed({
                    mainFragmentLoadingLayout.visibility = View.GONE
                }, 500)
                adapter = MainFragmentAdapter(object : OnItemViewClickListener {
                    override fun onItemViewClick(weather: Weather) {
                        val manager = activity?.supportFragmentManager
                        manager?.let { manager ->
                            val bundle = Bundle().apply {
                                putParcelable(DetailsFragment.BUNDLE_EXTRA, weather)
                            }
                            manager.beginTransaction()
                                .add(R.id.container, DetailsFragment.newInstance(bundle))
                                .addToBackStack("")
                                .commitAllowingStateLoss()
                        }
                    }
                }
                ).apply {
                    setWeather(appState.weatherData)
                }
                mainFragmentRecyclerView.adapter = adapter
            }
            is AppState.Loading -> {
                mainFragmentLoadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                mainFragmentLoadingLayout.visibility = View.GONE

                mainFragmentFAB.showSnackBar(
                    getString(R.string.error),
                    getString(R.string.reload),
                    { viewModel.getWeatherFromLocalSourceRus() }
                )
            }
        }
    }

    private fun startCalculations(seconds: Int): String {
        val date = Date()
        var diffInSec: Long
        do {
            val currentDate = Date()
            val diffInMs: Long = currentDate.time - date.time
            diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMs)
        } while (diffInSec < seconds)
        return diffInSec.toString()
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(weather: Weather)
    }

    companion object {
        private const val dataSetKey = "dataSetKey"
        fun newInstance() = MainFragment()
    }
}
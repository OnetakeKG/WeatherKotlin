import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.details_fragment.*
import net.nov.weatherkotlin.AppState
import net.nov.weatherkotlin.R
import net.nov.weatherkotlin.databinding.DetailsFragmentBinding
import net.nov.weatherkotlin.entities.Weather
import net.nov.weatherkotlin.ui.details.DetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsFragment : Fragment() {
    private lateinit var binding: DetailsFragmentBinding
    private val viewModel: DetailsViewModel by viewModel()
    private lateinit var weatherBundle: Weather

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weather = arguments?.getParcelable<Weather>(BUNDLE_EXTRA)
        weather?.let {
            with(binding) {
                cityName.text = it.city.city
                cityCoordinates.text = String.format(
                    getString(R.string.city_coordinates),
                    it.city.lat.toString(),
                    it.city.lon.toString()
                )
                viewModel.liveDataToObserve.observe(viewLifecycleOwner, { appState ->
                    when (appState) {
                        is AppState.Error -> {
                            //...
                            loadingLayout.visibility = View.GONE
                        }
                        AppState.Loading -> binding.loadingLayout.visibility = View.VISIBLE
                        is AppState.Success -> {
                            loadingLayout.visibility = View.GONE
                            temperatureValue.text = appState.weatherData[0].temperature?.toString()
                            feelsLikeValue.text = appState.weatherData[0].feelsLike?.toString()
                            weatherCondition.text = appState.weatherData[0].condition
                        }
                    }
                })
                viewModel.loadData(it.city)
                Picasso
                    .get()
                    .load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")
                    .fit()
                    .into(imageView)
                /*WeatherRepo.api.getWeather(it.city.lat, it.city.lon)
                    .enqueue(object : Callback<WeatherDTO> {
                        override fun onResponse(
                            call: Call<WeatherDTO>,
                            response: Response<WeatherDTO>
                        ) {
                            if(response.isSuccessful) {
                                val weather = response.body()?.let {
                                    Weather(
                                        temperature = it.fact.temp,
                                        feelsLike = it.fact.feels_like,
                                        condition = it.fact.condition
                                    )
                                } ?: Weather()
                                loadingLayout.visibility = View.GONE
                                temperatureValue.text = weather.temperature?.toString()
                                feelsLikeValue.text = weather.feelsLike?.toString()
                                weatherCondition.text = weather.condition
                            }
                        }

                        override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
                           //Запрос не прошел, или что-то другое на вашей стороне
                        }
                    })*/
            }
        }


    }

    companion object {
        private const val api_key = "7a436743-4c9e-415e-9edc-cc6b53f7c987"
        const val BUNDLE_EXTRA = "weather"

        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
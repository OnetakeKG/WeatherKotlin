package net.nov.weatherkotlin.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import net.nov.weatherkotlin.R
import net.nov.weatherkotlin.databinding.DetailsFragmentBinding
import net.nov.weatherkotlin.entities.Weather


    class DetailsFragment : Fragment() {
        private var _binding: DetailsFragmentBinding? = null
        private val binding get() = _binding!!

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _binding = DetailsFragmentBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            arguments?.getParcelable<Weather>(BUNDLE_EXTRA)?.let {
                with(binding) {
                    val city = it.city
                    cityName.text = city.city
                    cityCoordinates.text = String.format(
                        getString(R.string.city_coordinates),
                        city.lat.toString(),
                        city.lon.toString()
                    )
                    temperatureValue.text = it.temperature.toString()
                    feelsLikeValue.text = it.feelsLike.toString()
                }
            }
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }

        companion object {
            const val BUNDLE_EXTRA = "weather"

            fun newInstance(bundle: Bundle): DetailsFragment {
                val fragment = DetailsFragment()
                fragment.arguments = bundle
                return fragment
            }
        }
    }
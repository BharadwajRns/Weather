package com.example.weather.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.weather.R
import com.example.weather.databinding.FragmentWeatherOverViewBinding
import com.example.weather.db.entitys.WeatherEntity
import kotlinx.coroutines.launch

class WeatherOverViewFragment : Fragment() {

    private var binding : FragmentWeatherOverViewBinding? = null
    private var weatherDataFromIntent : WeatherEntity? = null
    init {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                binding?.weatherData = weatherDataFromIntent
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            weatherDataFromIntent = it.get("WEATHER_DATA") as WeatherEntity
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_weather_over_view,container,false)
        return binding?.root
    }
}
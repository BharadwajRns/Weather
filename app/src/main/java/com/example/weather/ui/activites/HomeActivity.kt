package com.example.weather.ui.activites

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather.R
import com.example.weather.databinding.ActivityHomeBinding
import com.example.weather.db.entitys.WeatherEntity
import com.example.weather.ui.adapters.LocationsRecyclerAdapter
import com.example.weather.ui.fragments.WeatherOverViewFragment
import com.example.weather.ui.uiStates.CurrentLocationUIState
import com.example.weather.ui.uiStates.FamousLocationUIState
import com.example.weather.ui.viewmodel.MainViewModel
import com.example.weather.utils.Constants.LOCATION_PERMISSION_REQUEST_CODE
import com.example.weather.utils.UnixTimeToDate
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private var binding  : ActivityHomeBinding? = null
    private val viewModel : MainViewModel by viewModels()
    private lateinit var adapter: LocationsRecyclerAdapter
    private var weatherData : WeatherEntity? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        binding?.famousRecycler.let {
            it?.layoutManager = LinearLayoutManager(this)
        }

        viewModel.isConnected.observe(this) {
            if(it) {
                binding?.offlineLyt?.visibility = View.GONE
                checkLocationPermission()
                viewModel.getFamousLocationData()
            }else{
                binding?.offlineLyt?.visibility = View.VISIBLE
                binding?.loadingLyt?.visibility = View.GONE
                binding?.famousRecycler?.visibility = View.GONE
                viewModel.getOfflineDataFromRoom().observe(this) {
                    binding?.apply {
                        this.tvTimeZone.text = it.timezone
                        this.tvTemperature.text = it.current.temp.toString().plus(" c")
                        this.tvFeelsLike.text = it.current.feels_like.toString().plus(" c")
                        this.tvCurrentDate.text = UnixTimeToDate().convertUnixTimestampToDate(it.current.dt)
                        this.tvWindSpeed.text = it.current.wind_speed.toString()
                        this.tvClouds.text = it.current.clouds.toString().plus(" %")

                    }
                }
                Toast.makeText(this," not connected",Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.currentLiveData.observe(this) {
            when(it) {
                is CurrentLocationUIState.Loading -> {

                }
                is CurrentLocationUIState.Success -> {
                    binding?.apply {
                        this.tvTimeZone.text = it.weatherData.timezone
                        this.tvTemperature.text = it.weatherData.current.temp.toString().plus(" c")
                        this.tvFeelsLike.text = it.weatherData.current.feels_like.toString().plus(" c")
                        this.tvCurrentDate.text = UnixTimeToDate().convertUnixTimestampToDate(it.weatherData.current.dt)
                        this.tvWindSpeed.text = it.weatherData.current.wind_speed.toString()
                        this.tvClouds.text = it.weatherData.current.clouds.toString().plus(" %")
                    }
                    weatherData = it.weatherData
                    viewModel.insertIntoDb(it.weatherData)
                }
                is CurrentLocationUIState.Error -> {

                }

            }
        }

        viewModel.famousLocationsLiveData.observe(this) {
            when(it) {
                is FamousLocationUIState.Loading -> {
                    binding?.loadingLyt?.visibility = View.VISIBLE
                }
                is FamousLocationUIState.Success -> {
                    binding?.loadingLyt?.visibility = View.GONE
                    binding?.famousRecycler?.visibility = View.VISIBLE
                    adapter = LocationsRecyclerAdapter {weatherEntity ->
                        val fragment = WeatherOverViewFragment()
                        val bundle = Bundle()
                        bundle.putSerializable("WEATHER_DATA",weatherEntity)
                        fragment.arguments = bundle
                        supportFragmentManager.
                        beginTransaction()
                            .replace(R.id.container,fragment)
                            .addToBackStack(null)
                            .commit()
                    }.also {adapter->
                        adapter.setItems(it.weatherData)
                        binding?.famousRecycler?.adapter = adapter
                    }
                }
                is FamousLocationUIState.Error -> {

                }
            }
        }

        binding?.cvCurrentLocation?.setOnClickListener {_->
            val fragment = WeatherOverViewFragment()
            val bundle = Bundle()
            bundle.putSerializable("WEATHER_DATA",weatherData)
            fragment.arguments = bundle
            supportFragmentManager.
            beginTransaction()
                .replace(R.id.container,fragment)
                .addToBackStack(null)
                .commit()
        }

        setContentView(binding?.root)
    }

    private fun checkLocationPermission() {
        if(ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION)
             != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)  {
             ActivityCompat.requestPermissions(this,
                 arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                     Manifest.permission.ACCESS_COARSE_LOCATION),
                 LOCATION_PERMISSION_REQUEST_CODE
             )
        }else{
            getCurrentLocation()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else {
                Toast.makeText(this,"we need location permission",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getCurrentLocation() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                viewModel.getDataFromApi(
                    latitude,
                    longitude
                )
            } else {
                Toast.makeText(this,"unable to get your location",Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception: Exception ->

        }
    }

}
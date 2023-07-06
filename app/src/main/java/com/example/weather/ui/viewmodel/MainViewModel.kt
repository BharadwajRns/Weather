package com.example.weather.ui.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.DisplayMetrics
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.db.entitys.WeatherEntity
import com.example.weather.respository.MainRepository
import com.example.weather.ui.uiStates.CurrentLocationUIState
import com.example.weather.ui.uiStates.FamousLocationUIState
import com.example.weather.utils.Constants.API_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import retrofit2.Response
import javax.inject.Inject


/*
    home activity view model
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    application: Application
) : ViewModel() {

    //live data for current location
    private val _currentLocationMutableLiveData = MutableLiveData<CurrentLocationUIState>()
    val currentLiveData : LiveData<CurrentLocationUIState>
        get() = _currentLocationMutableLiveData

    //live data for famous locations
    private val _famousLocationMutableLiveData = MutableLiveData<FamousLocationUIState>()
    val famousLocationsLiveData : LiveData<FamousLocationUIState>
        get() = _famousLocationMutableLiveData

    //livedata for connection
    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean> = _isConnected

    //connectivity manager to get the network call back
    private val connectivityManager =
        application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    //n/w callback
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _isConnected.postValue(true)
        }

        override fun onLost(network: Network) {
            _isConnected.postValue(false)
        }
    }

    //just some routine calls you can ignore this
    init {
        checkInitialConnectivityState()
        registerNetworkCallback()
    }

    //function to get the current location
    fun getDataFromApi(lat : Double,lon : Double) {
        _currentLocationMutableLiveData.value = CurrentLocationUIState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val response = mainRepository.getWeatherFromApi(
                API_KEY,
                lat,
                lon,
                "hourly,daily,minutely",
                "metric"
            )
            if(response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    _currentLocationMutableLiveData.value = CurrentLocationUIState.Success(response.body()!!)
                }
            }else{
                withContext(Dispatchers.Main) {
                    withContext(Dispatchers.Main) {
                        _currentLocationMutableLiveData.value = CurrentLocationUIState.Error("Unable to fetch data")
                    }
                }
            }
        }
    }

    /*
       This fun is huge, but it gives a shot
       function to  call 5 responses asynchronously
       and display it in the recycler view
     */
    fun getFamousLocationData() {
        _famousLocationMutableLiveData.value = FamousLocationUIState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val asyncResponse = viewModelScope.async {
                val responses : MutableList<Response<WeatherEntity>?> = mutableListOf()
                //New York
                val response1 = withTimeoutOrNull(15000L) {
                    mainRepository.getWeatherFromApi(
                        API_KEY,
                        40.7128,
                        -74.0060,
                        "hourly,daily,minutely",
                        "metric"
                    )
                }

                //Singapore
                val response2 = withTimeoutOrNull(15000L) {
                    mainRepository.getWeatherFromApi(
                        API_KEY,
                        1.3521,
                        103.8198,
                        "hourly,daily,minutely",
                        "metric"
                    )
                }

                //sydney
                val response3 = withTimeoutOrNull(15000L) {
                    mainRepository.getWeatherFromApi(
                        API_KEY,
                        33.8688,
                        151.2093,
                        "hourly,daily,minutely",
                        "metric"
                    )
                }

                //Melbourne
                val response4 = withTimeoutOrNull(15000L) {
                    mainRepository.getWeatherFromApi(
                        API_KEY,
                        37.8136,
                        144.9631,
                        "hourly,daily,minutely",
                        "metric"
                    )
                }

                //Mumbai
                val response5 = withTimeoutOrNull(15000L) {
                    mainRepository.getWeatherFromApi(
                        API_KEY,
                        19.0760,
                        72.8777,
                        "hourly,daily,minutely",
                        "metric"
                    )
                }

                /*
                     Returning the mutableList of
                     responses asynchronously
                 */
                responses.apply {
                    this.add(0,response1)
                    this.add(1,response2)
                    this.add(2,response3)
                    this.add(3,response4)
                    this.add(4,response4)
                    this.add(5,response5)
                }
            }
            //finishing the response
            val response = asyncResponse.await()

            //checking weather the response is null or not
            if(response[0] != null
                && response[1] != null
                && response[2] != null
                && response[3] != null
                && response[4] != null
                && response[5] != null
                ) {

                /*
                    All the responses is not null
                    so time to check weather they are
                    successful or not
                 */
                response[0].let {response0->
                    response[1].let {response1->
                        response[2].let {response2->
                            response[3].let {response3->
                                response[4].let {response4->
                                    response[5].let {response5->
                                        if(
                                            response0?.isSuccessful!!
                                            && response1?.isSuccessful!!
                                            && response2?.isSuccessful!!
                                            && response3?.isSuccessful!!
                                            && response4?.isSuccessful!!
                                            && response5?.isSuccessful!!
                                        ) {

                                            /*
                                                All the responses is successful
                                                now time to get their body
                                             */
                                            val weatherDataList = mutableListOf<WeatherEntity>()
                                            response0.body()?.let {response0->
                                                response1.body()?.let {response1->
                                                    response2.body()?.let {response2->
                                                        response3.body()?.let {response3->
                                                            response4.body()?.let {response4->
                                                                response5.body()?.let {response5->

                                                                    /*
                                                                       Inserting all the responses into
                                                                       a mutable list to pass through the
                                                                       live data
                                                                     */
                                                                    weatherDataList.apply {
                                                                        this.add(0,response0)
                                                                        this.add(1,response1)
                                                                        this.add(2,response2)
                                                                        this.add(3,response3)
                                                                        this.add(4,response4)
                                                                        this.add(5,response5)
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            // updating the ui state
                                            withContext(Dispatchers.Main) {
                                                _famousLocationMutableLiveData.value =
                                                    FamousLocationUIState.Success(
                                                        weatherDataList
                                                    )
                                            }
                                        }else{
                                            withContext(Dispatchers.Main) {
                                                _famousLocationMutableLiveData.value =
                                                    FamousLocationUIState.Error(
                                                        "Unable to fetch"
                                                    )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /*
        this function triggers when we launch the
        application to check the internet state
     */
    private fun checkInitialConnectivityState() {
        val networkInfo = connectivityManager.activeNetworkInfo
        val isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting
        _isConnected.postValue(isConnected)
    }

    /*
        triggers during the application alive
        to check the network states
     */
    private fun registerNetworkCallback() {
        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    /*
       insert the data into room
     */
    fun insertIntoDb(weatherEntity: WeatherEntity) {
        viewModelScope.launch(Dispatchers.IO){
            mainRepository.insertWeatherDataIntoRoom(weatherEntity)
        }
    }

    /*
        retrieve the offline data form room
     */
    fun getOfflineDataFromRoom() = mainRepository.getWeatherDataFromRoom()

}
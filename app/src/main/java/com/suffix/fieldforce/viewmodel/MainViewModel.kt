package com.suffix.fieldforce.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.suffix.fieldforce.model.LocationResponse
import com.suffix.fieldforce.networking.FieldForceApi
import com.suffix.fieldforce.networking.FieldForceApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _locationResponse = MutableLiveData<LocationResponse>()
    val locationResponse: LiveData<LocationResponse>
        get() = _locationResponse

    private val _status = MutableLiveData<FieldForceApiStatus>()
    val status: LiveData<FieldForceApiStatus>
        get() = _status

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun sendGeoLocation(key: String, userId: String, lat: String, lng: String) {
        coroutineScope.launch {
            val sendGeoLocationDeferred = FieldForceApi.retrofitService.sendGeoLocationAsync(
                key,
                userId,
                lat,
                lng
            )

            try {
                _status.value = FieldForceApiStatus.LOADING
                val result = sendGeoLocationDeferred.await()
                _status.value = FieldForceApiStatus.DONE
                _locationResponse.value = result
            } catch (e: Exception) {
                _status.value = FieldForceApiStatus.ERROR
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
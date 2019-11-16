package com.suffix.fieldforce.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.suffix.fieldforce.model.LocationResponse
import com.suffix.fieldforce.networking.FieldForceApi
import com.suffix.fieldforce.networking.FieldForceApiStatus
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : BaseViewModel(application) {
    private val _locationResponse = MutableLiveData<LocationResponse>()
    val locationResponse: LiveData<LocationResponse>
        get() = _locationResponse

    private val _status = MutableLiveData<FieldForceApiStatus>()
    val status: LiveData<FieldForceApiStatus>
        get() = _status

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
}

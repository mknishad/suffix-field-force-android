package com.suffix.fieldforce.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.suffix.fieldforce.model.BillDetailsResponseData
import com.suffix.fieldforce.networking.FieldForceApi
import com.suffix.fieldforce.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error

class BillDetailsViewModel(application: Application): AndroidViewModel(application), AnkoLogger {
    private val _billDetails = MutableLiveData<BillDetailsResponseData>()
    val billDetails: LiveData<BillDetailsResponseData>
    get() = _billDetails

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean>
    get() = _progress

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun getBillDetails(billId: String) {
        coroutineScope.launch {
            val getBillDetailsDeferred = FieldForceApi.retrofitService.getBillDetailsAsync(
                Constants.KEY,
                Constants.USER_ID,
                "23.7746479",
                "90.4031033",
                billId
            )

            try {
                _progress.value = true
                val result = getBillDetailsDeferred.await()
                _billDetails.value = result.responseData
                _progress.value = false
            } catch (e: Exception) {
                error(e.message)
                _progress.value = false
            }
        }
    }
}

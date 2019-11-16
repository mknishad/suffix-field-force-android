package com.suffix.fieldforce.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.suffix.fieldforce.R
import com.suffix.fieldforce.model.BillDetailsResponseData
import com.suffix.fieldforce.networking.FieldForceApi
import com.suffix.fieldforce.preference.FieldForcePreferences
import com.suffix.fieldforce.util.Constants
import kotlinx.coroutines.launch
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error

class BillDetailsViewModel(application: Application): BaseViewModel(application), AnkoLogger {
    private val _billDetails = MutableLiveData<BillDetailsResponseData>()
    val billDetails: LiveData<BillDetailsResponseData>
    get() = _billDetails

    private val preferences: FieldForcePreferences = FieldForcePreferences(application)

    fun getBillDetails(billId: String) {
        progress.value = true
        coroutineScope.launch {
            val getBillDetailsDeferred = FieldForceApi.retrofitService.getBillDetailsAsync(
                Constants.KEY,
                Constants.USER_ID,
                preferences.getLocation().latitude.toString(),
                preferences.getLocation().longitude.toString(),
                billId
            )

            try {
                val result = getBillDetailsDeferred.await()
                _billDetails.value = result.responseData
                progress.value = false
            } catch (e: Exception) {
                error(e.message, e)
                progress.value = false
                message.value = getApplication<Application>().resources.getString(R.string.something_went_wrong)
            }
        }
    }
}

package com.suffix.fieldforce.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.suffix.fieldforce.R
import com.suffix.fieldforce.model.BillDetailsResponseData
import com.suffix.fieldforce.networking.FieldForceApi
import com.suffix.fieldforce.util.Constants
import kotlinx.coroutines.launch
import org.jetbrains.anko.error

class RequisitionDetailsViewModel(application: Application) : BaseViewModel(application) {
  private val _requisitionDetails = MutableLiveData<BillDetailsResponseData>()
  val requisitionDetails: LiveData<BillDetailsResponseData>
    get() = _requisitionDetails

  fun getRequisitionDetails(requisitionId: String) {
    progress.value = true
    coroutineScope.launch {
      try {
        val getRequisitionDetailsDeferred =
          FieldForceApi.retrofitService.getRequisitionDetailsAsync(
            Constants.KEY,
            preferences.getUser().userId,
            preferences.getLocation().latitude.toString(),
            preferences.getLocation().longitude.toString(),
            requisitionId
          )

        val result = getRequisitionDetailsDeferred.await()
        if (result.responseCode == "1") {
          _requisitionDetails.value = result.responseData
        } else {
          message.value = result.responseText
        }
        progress.value = false
      } catch (e: Exception) {
        error(e.message, e)
        progress.value = false
        message.value =
          getApplication<Application>().resources.getString(R.string.something_went_wrong)
      }
    }
  }
}

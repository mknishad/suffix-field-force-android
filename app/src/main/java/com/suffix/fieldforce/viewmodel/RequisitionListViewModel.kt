package com.suffix.fieldforce.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.suffix.fieldforce.R
import com.suffix.fieldforce.model.Requisition
import com.suffix.fieldforce.networking.FieldForceApi
import com.suffix.fieldforce.util.Constants
import kotlinx.coroutines.launch
import org.jetbrains.anko.error

class RequisitionListViewModel(application: Application) : BaseViewModel(application) {
  private val _requisitionList = MutableLiveData<List<Requisition>>()
  val requisitionList: LiveData<List<Requisition>>
    get() = _requisitionList

  init {
    getRequisitionList()
  }

  private fun getRequisitionList() {
    progress.value = true
    coroutineScope.launch {
      try {
        val getRequisitionListDeferred = FieldForceApi.retrofitService.getRequisitionListAsync(
          Constants.KEY,
          preferences.getUser().userId,
          preferences.getLocation().latitude.toString(),
          preferences.getLocation().longitude.toString()
        )
        val result = getRequisitionListDeferred.await()
        if (result.responseCode == "1") {
          if (result.responseData.itemRequisitionObj.responseCode == "1") {
            _requisitionList.value = result.responseData.itemRequisitionObj.responseData
          } else {
            message.value = result.responseData.itemRequisitionObj.responseText
          }
        } else {
          message.value = result.responseText
        }
      } catch (e: Exception) {
        error(e.message, e)
        message.value = getApplication<Application>().resources
          .getString(R.string.something_went_wrong)
      } finally {
        progress.value = false
      }
    }
  }
}
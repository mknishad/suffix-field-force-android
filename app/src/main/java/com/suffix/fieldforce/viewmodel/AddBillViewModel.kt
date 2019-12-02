package com.suffix.fieldforce.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.suffix.fieldforce.R
import com.suffix.fieldforce.model.AddBillResponse
import com.suffix.fieldforce.model.BillData
import com.suffix.fieldforce.model.BillType
import com.suffix.fieldforce.networking.FieldForceApi
import com.suffix.fieldforce.util.Constants
import kotlinx.coroutines.launch
import org.jetbrains.anko.error

class AddBillViewModel(application: Application) : BaseViewModel(application) {
  private val _billTypes = MutableLiveData<List<BillType>>()
  val billTypes: LiveData<List<BillType>>
    get() = _billTypes

  private val _addBillResponse = MutableLiveData<AddBillResponse>()
  val addBillResponse: LiveData<AddBillResponse>
    get() = _addBillResponse

  private val _eventAddBill = MutableLiveData<Boolean>()
  val eventAddBill: LiveData<Boolean>
    get() = _eventAddBill

  init {
    getBillTypes()
  }

  private fun getBillTypes() {
    progress.value = true
    coroutineScope.launch {
      try {
        val getBillTypesDeferred = FieldForceApi.retrofitService.getBillTypeAsync(
          Constants.KEY,
          preferences.getUser().userId,
          preferences.getLocation().latitude.toString(),
          preferences.getLocation().longitude.toString()
        )

        val result = getBillTypesDeferred.await()
        _billTypes.value = result.responseData
        progress.value = false
      } catch (e: Exception) {
        error(e.message, e)
        progress.value = false
        message.value =
          getApplication<Application>().resources.getString(R.string.something_went_wrong)
      }
    }
  }

  fun submitBillWithAdvanceId(
    key: String, userId: String, lat: String, lng: String, billData: BillData,
    encodedImage: String, taskId: String, priority: String
  ) {
    var advanceId: String = "0"
    coroutineScope.launch {
      try {
        val advanceIdDeferred = FieldForceApi.retrofitService.getTaskWiseAdvanceIdAsync(
          key,
          userId,
          lat,
          lng,
          taskId
        )

        progress.value = true
        val result = advanceIdDeferred.await()
        if (result.responseCode.equals("1", true)) {
          if (result.responseData.responseCode.equals("1", true)) {
            if (result.responseData.bills.isNotEmpty()) {
              advanceId = result.responseData.bills[0].advanceId.toString()
            }
          }
        }
      } catch (e: Exception) {
        error(e.message, e)
      }

      submitBill(key, userId, lat, lng, billData, taskId, encodedImage, advanceId, priority)
    }
  }

  private fun submitBill(
    key: String, userId: String, lat: String, lng: String, billData: BillData,
    taskId: String, encodedImage: String, advanceId: String, priority: String
  ) {
    val billDataJson = Gson().toJson(billData)

    coroutineScope.launch {
      try {
        val addBillDeferred = FieldForceApi.retrofitService.addBillAsync(
          key,
          userId,
          lat,
          lng,
          billDataJson,
          taskId,
          encodedImage,
          advanceId,
          priority
        )

        val result = addBillDeferred.await()
        if (result.responseCode.equals("1", true)) {
          _addBillResponse.value = result
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

  fun submitAdvanceBill(
    key: String, userId: String, lat: String, lng: String, billData: BillData, encodedImage: String,
    taskId: String, priority: String
  ) {
    val billDataJson = Gson().toJson(billData)

    coroutineScope.launch {
      try {
        val addAdvanceBillDeferred = FieldForceApi.retrofitService.addAdvanceBillAsync(
          key,
          userId,
          lat,
          lng,
          billDataJson,
          encodedImage,
          taskId,
          priority
        )

        progress.value = true
        val result = addAdvanceBillDeferred.await()
        if (result.responseCode.equals("1", true)) {
          _addBillResponse.value = result
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

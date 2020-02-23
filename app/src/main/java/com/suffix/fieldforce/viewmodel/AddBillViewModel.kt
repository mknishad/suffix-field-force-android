package com.suffix.fieldforce.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.suffix.fieldforce.R
import com.suffix.fieldforce.model.*
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

  private val _taskList = MutableLiveData<List<Task>>()
  val taskList: LiveData<List<Task>>
    get() = _taskList

  private val _eventAddBill = MutableLiveData<Boolean>()
  val eventAddBill: LiveData<Boolean>
    get() = _eventAddBill

  init {
    getBillTypes()
    getTaskList()
  }

  private fun getTaskList() {
    progress.value = true
    coroutineScope.launch {
      try {
        val getTaskListDeferred = FieldForceApi.retrofitService.getTaskListForBillAsync(
          Constants.KEY,
          preferences.getUser().userId,
          preferences.getLocation().latitude.toString(),
          preferences.getLocation().longitude.toString()
        )

        val result = getTaskListDeferred.await()
        if (result.responseCode == "1") {
          _taskList.value = result.responseData
          progress.value = false
        } else {
          message.value = result.responseText
        }
      } catch (e: Exception) {
        error(e.message, e)
        progress.value = false
        message.value =
          getApplication<Application>().resources.getString(R.string.something_went_wrong)
      }
    }
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
    encodedImage: String, taskIdData: TaskIdData, priority: String
  ) {
    var advanceId = "0"
    val taskIdDataJson = Gson().toJson(taskIdData)
    /*coroutineScope.launch {
      try {
        val advanceIdDeferred = FieldForceApi.retrofitService.getTaskWiseAdvanceIdAsync(
          key,
          userId,
          lat,
          lng,
          taskIdDataJson
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
    }*/
    submitBill(key, userId, lat, lng, billData, taskIdData, encodedImage, advanceId, priority)
  }

  private fun submitBill(
    key: String, userId: String, lat: String, lng: String, billData: BillData,
    taskIdData: TaskIdData, encodedImage: String, advanceId: String, priority: String
  ) {
    val billDataJson = Gson().toJson(billData)
    val taskIdDataJson = Gson().toJson(taskIdData)

    coroutineScope.launch {
      try {
        val addBillDeferred = FieldForceApi.retrofitService.addBillAsync(
          key,
          userId,
          lat,
          lng,
          billDataJson,
          taskIdDataJson,
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
    taskIdData: TaskIdData, priority: String
  ) {
    val billDataJson = Gson().toJson(billData)
    val taskIdDataJson = Gson().toJson(taskIdData)

    coroutineScope.launch {
      try {
        val addAdvanceBillDeferred = FieldForceApi.retrofitService.addAdvanceBillAsync(
          key,
          userId,
          lat,
          lng,
          billDataJson,
          encodedImage,
          taskIdDataJson,
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

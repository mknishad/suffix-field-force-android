package com.suffix.fieldforce.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.suffix.fieldforce.R
import com.suffix.fieldforce.model.CreateRequisitionResponse
import com.suffix.fieldforce.model.ItemRequisitionData
import com.suffix.fieldforce.model.Task
import com.suffix.fieldforce.model.TaskIdData
import com.suffix.fieldforce.networking.FieldForceApi
import com.suffix.fieldforce.util.Constants
import kotlinx.coroutines.launch
import org.jetbrains.anko.error

class CreateRequisitionViewModel(application: Application) : BaseViewModel(application) {

  private val _createRequisitionResponse = MutableLiveData<CreateRequisitionResponse>()
  val createRequisitionResponse: LiveData<CreateRequisitionResponse>
    get() = _createRequisitionResponse

  private val _taskList = MutableLiveData<List<Task>>()
  val taskList: LiveData<List<Task>>
    get() = _taskList

  init {
    getTaskList()
  }

  private fun getTaskList() {
    progress.value = true
    coroutineScope.launch {
      try {
        val getTaskListDeferred = FieldForceApi.retrofitService.getRequisitionSearchTaskAsync(
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

  fun createRequisition(
    taskIdData: TaskIdData,
    itemRequisitionData: ItemRequisitionData,
    priority: String
  ) {
    progress.value = true
    val taskIdDataJson = Gson().toJson(taskIdData)
    val itemRequisitionDataJson = Gson().toJson(itemRequisitionData)
    coroutineScope.launch {
      try {
        val createRequisitionDeferred = FieldForceApi.retrofitService.createRequisitionAsync(
          Constants.KEY,
          preferences.getUser().userId,
          preferences.getLocation().latitude.toString(),
          preferences.getLocation().longitude.toString(),
          taskIdDataJson,
          itemRequisitionDataJson,
          priority
        )

        val result = createRequisitionDeferred.await()
        if (result.responseCode == "1") {
          _createRequisitionResponse.value = result
          progress.value = false
          message.value = result.responseText
        } else {
          message.value = result.responseText
        }
        _createRequisitionResponse.value = result
      } catch (e: Exception) {
        error(e.message, e)
        progress.value = false
        message.value =
          getApplication<Application>().resources.getString(R.string.something_went_wrong)
      }
    }
  }
}

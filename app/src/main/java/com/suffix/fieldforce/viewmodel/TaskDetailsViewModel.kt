package com.suffix.fieldforce.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.suffix.fieldforce.R
import com.suffix.fieldforce.model.Task
import com.suffix.fieldforce.networking.FieldForceApi
import kotlinx.coroutines.launch
import org.jetbrains.anko.error

class TaskDetailsViewModel(application: Application) : BaseViewModel(application) {

  private val _task = MutableLiveData<Task>()
  val task: LiveData<Task>
    get() = _task


  fun getTaskDetails(taskId: String) {
    progress.value = true
    viewModelScope.launch {
      try {
        val getTaskDetailsDeferred = FieldForceApi.retrofitService.getTaskDetailsAsync(
          preferences.getUser().userId,
          taskId
        )

        val result = getTaskDetailsDeferred.await()
        if (result[0].responseCode.equals("1", true)) {
          _task.value = result[0].responseData[0]
        } else {
          message.value = result[0].responseMsg
        }
        progress.value = false
      } catch (e: Exception) {
        progress.value = false
        error(e.message, e)
        message.value = getApplication<Application>().resources
          .getString(R.string.something_went_wrong)
      }
    }
  }
}

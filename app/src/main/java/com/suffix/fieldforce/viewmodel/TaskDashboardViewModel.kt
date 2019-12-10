package com.suffix.fieldforce.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.suffix.fieldforce.R
import com.suffix.fieldforce.model.TaskDashboardResponseData
import com.suffix.fieldforce.networking.FieldForceApi
import kotlinx.coroutines.launch
import org.jetbrains.anko.error
import org.jetbrains.anko.info

class TaskDashboardViewModel(application: Application) : BaseViewModel(application) {
  private val _taskDashboard = MutableLiveData<TaskDashboardResponseData>()
  val taskDashboard: LiveData<TaskDashboardResponseData>
    get() = _taskDashboard

  private val _eventShowAssignedTaskList = MutableLiveData<Boolean>()
  val eventShowAssignedTaskList: LiveData<Boolean>
    get() = _eventShowAssignedTaskList

  private val _eventShowAcceptedTaskList = MutableLiveData<Boolean>()
  val eventShowAcceptedTaskList: LiveData<Boolean>
    get() = _eventShowAcceptedTaskList

  private val _eventShowCompletedTaskList = MutableLiveData<Boolean>()
  val eventShowCompletedTaskList: LiveData<Boolean>
    get() = _eventShowCompletedTaskList

  private val _eventShowInProgressTaskList = MutableLiveData<Boolean>()
  val eventShowInProgressTaskList: LiveData<Boolean>
    get() = _eventShowInProgressTaskList

  init {
    getTaskDashboard()
  }

  private fun getTaskDashboard() {
    progress.value = true
    coroutineScope.launch {
      try {
        val getTaskDashboardDeferred = FieldForceApi.retrofitService.getTaskDashboardAsync(
          preferences.getUser().userId
        )

        val result = getTaskDashboardDeferred.await()
        if (result[0].responseCode.equals("1", true)) {
          _taskDashboard.value = result[0].responseData[0]
          info(_taskDashboard.value?.acceptedTicketCount)
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

  fun showAssignedTaskList() {
    _eventShowAssignedTaskList.value = true
  }

  fun assignedTaskListShown() {
    _eventShowAssignedTaskList.value = false
  }

  fun showAcceptedTaskList() {
    _eventShowAcceptedTaskList.value = true
  }

  fun acceptedTaskListShown() {
    _eventShowAcceptedTaskList.value = false
  }

  fun showCompletedTaskList() {
    _eventShowCompletedTaskList.value = true
  }

  fun completedTaskListShown() {
    _eventShowCompletedTaskList.value = false
  }

  fun showInProgressTaskList() {
    _eventShowInProgressTaskList.value = true
  }

  fun inProgressTaskListShown() {
    _eventShowInProgressTaskList.value = false
  }
}
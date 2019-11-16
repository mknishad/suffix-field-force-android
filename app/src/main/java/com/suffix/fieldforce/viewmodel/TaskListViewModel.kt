package com.suffix.fieldforce.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.suffix.fieldforce.R
import com.suffix.fieldforce.model.Task
import com.suffix.fieldforce.model.TaskListResponse
import com.suffix.fieldforce.networking.FieldForceApi
import com.suffix.fieldforce.util.Constants
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch
import org.jetbrains.anko.error
import org.jetbrains.anko.info

class TaskListViewModel(application: Application) : BaseViewModel(application) {
    private val _taskList = MutableLiveData<List<Task>>()
    val taskList: LiveData<List<Task>>
        get() = _taskList

    fun getTaskList(userId: String, taskType: String) {
        progress.value = true
        val getTaskListDeferred: Deferred<List<TaskListResponse>>
        when (taskType) {
            Constants.ASSIGNED -> {
                getTaskListDeferred =
                    FieldForceApi.retrofitService.getAssignedTaskListAsync(
                        preferences.getUser().userId
                    )
            }
            Constants.ACCEPTED -> {
                getTaskListDeferred =
                    FieldForceApi.retrofitService.getAcceptedTaskListAsync(
                        preferences.getUser().userId
                    )
            }
            Constants.COMPLETED -> {
                getTaskListDeferred =
                    FieldForceApi.retrofitService.getCompletedTaskListAsync(
                        preferences.getUser().userId
                    )
            }
            else -> {
                getTaskListDeferred =
                    FieldForceApi.retrofitService.getInProgressTaskListAsync(
                        preferences.getUser().userId
                    )
            }
        }
        coroutineScope.launch {
            try {
                val result = getTaskListDeferred.await()
                if (result[0].responseCode.equals("1", true)) {
                    _taskList.value = result[0].responseData
                    info(_taskList.value)
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

package com.suffix.fieldforce.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.suffix.fieldforce.R
import com.suffix.fieldforce.model.TaskDashboardResponseData
import com.suffix.fieldforce.networking.FieldForceApi
import com.suffix.fieldforce.util.Constants
import kotlinx.coroutines.launch
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info

class TaskDashboardViewModel(application: Application): BaseViewModel(application), AnkoLogger {
    private val _taskDashboard = MutableLiveData<TaskDashboardResponseData>()
    val taskDashboard: LiveData<TaskDashboardResponseData>
    get() = _taskDashboard

    init {
        getTaskDashboard()
    }

    private fun getTaskDashboard() {
        coroutineScope.launch {
            val getTaskDashboardDeferred = FieldForceApi.retrofitService.getTaskDashboardAsync(
                Constants.USER_ID
            )

            try {
                _progress.value = true
                val result = getTaskDashboardDeferred.await()
                if (result[0].responseCode.equals("1", true)) {
                    _taskDashboard.value = result[0].responseData[0]
                    info(_taskDashboard.value?.acceptedTicketCount)
                } else {
                    _message.value = result[0].responseMsg
                }
                _progress.value = false
            } catch (e: Exception) {
                error(e.message, e)
                _progress.value = false
                _message.value = getApplication<Application>().resources
                    .getString(R.string.something_went_wrong)
            }
        }
    }
}
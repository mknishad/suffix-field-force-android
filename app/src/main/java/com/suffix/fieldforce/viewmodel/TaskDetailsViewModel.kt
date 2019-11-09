package com.suffix.fieldforce.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.suffix.fieldforce.R
import com.suffix.fieldforce.model.Task
import com.suffix.fieldforce.networking.FieldForceApi
import com.suffix.fieldforce.util.Constants
import kotlinx.coroutines.launch
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error

class TaskDetailsViewModel(application: Application): BaseViewModel(application), AnkoLogger {

    private val _task = MutableLiveData<Task>()
    val task: LiveData<Task>
    get() = _task


    fun getTaskDetails(taskId: String) {
        viewModelScope.launch {
            val  getTaskDetailsDeferred = FieldForceApi.retrofitService.getTaskDetailsAsync(
                Constants.USER_ID,
                taskId
            )

            try {
                progress.value = true
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
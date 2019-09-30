package com.suffix.fieldforce.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val _eventNavigateToTask = MutableLiveData<Boolean>()
    val eventNavigateToTask: LiveData<Boolean>
        get() = _eventNavigateToTask

    fun showTask() {
        _eventNavigateToTask.value = true
    }

    fun taskShown() {
        _eventNavigateToTask.value = false
    }
}

package com.suffix.fieldforce.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

open class BaseViewModel(application: Application): AndroidViewModel(application) {
    val _progress = MutableLiveData<Boolean>()

    protected val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    private val viewModelJob = Job()
    protected val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
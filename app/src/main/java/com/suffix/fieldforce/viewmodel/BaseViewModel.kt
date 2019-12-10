package com.suffix.fieldforce.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.suffix.fieldforce.preference.FieldForcePreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.jetbrains.anko.AnkoLogger

open class BaseViewModel(application: Application) : AndroidViewModel(application), AnkoLogger {
  val progress = MutableLiveData<Boolean>()
  val message = MutableLiveData<String>()
  val preferences = FieldForcePreferences(application)

  private val viewModelJob = Job()
  protected val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

  override fun onCleared() {
    super.onCleared()
    viewModelJob.cancel()
  }
}

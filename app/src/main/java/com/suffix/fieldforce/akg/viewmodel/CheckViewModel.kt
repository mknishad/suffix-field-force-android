package com.suffix.fieldforce.akg.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.suffix.fieldforce.viewmodel.BaseViewModel

class CheckViewModel(application: Application) : BaseViewModel(application) {
  private val _eventShowPC = MutableLiveData<Boolean>()
  val eventShowPC: LiveData<Boolean>
    get() = _eventShowPC

  private val _eventShowMonitor = MutableLiveData<Boolean>()
  val eventShowMonitor: LiveData<Boolean>
    get() = _eventShowMonitor

  private val _eventShowRouter = MutableLiveData<Boolean>()
  val eventShowRouter: LiveData<Boolean>
    get() = _eventShowRouter

  private val _eventShowSpeaker = MutableLiveData<Boolean>()
  val eventShowSpeaker: LiveData<Boolean>
    get() = _eventShowSpeaker

  fun showPC() {
    _eventShowPC.value = true
  }

  fun pcShown() {
    _eventShowPC.value = false
  }

  fun showMonitor() {
    _eventShowMonitor.value = true
  }

  fun monitorShown() {
    _eventShowMonitor.value = false
  }

  fun showRouter() {
    _eventShowRouter.value = true
  }

  fun routerShown() {
    _eventShowRouter.value = false
  }

  fun showSpeaker() {
    _eventShowSpeaker.value = true
  }

  fun speakerShown() {
    _eventShowSpeaker.value = false
  }
}

package com.suffix.fieldforce.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class BillsViewModel(application: Application) : AndroidViewModel(application) {
    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean>
        get() = _progress

    private val _eventGoToAddBills = MutableLiveData<Boolean>()
    val eventShowAddBills: LiveData<Boolean>
        get() = _eventGoToAddBills

    fun showAddBills() {
        _eventGoToAddBills.value = true
    }

    fun addBillsShown() {
        _eventGoToAddBills.value = false
    }
}
package com.suffix.fieldforce.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.suffix.fieldforce.model.BillDashboardResponseData
import com.suffix.fieldforce.networking.FieldForceApi
import com.suffix.fieldforce.preference.FieldForcePreferences
import com.suffix.fieldforce.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class BillsViewModel(application: Application) : AndroidViewModel(application) {
    private val _billsDashboard = MutableLiveData<BillDashboardResponseData>()
    val billsDashboard: LiveData<BillDashboardResponseData>
        get() = _billsDashboard

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean>
        get() = _progress

    private val _eventGoToAddBills = MutableLiveData<Boolean>()
    val eventShowAddBills: LiveData<Boolean>
        get() = _eventGoToAddBills

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val preferences: FieldForcePreferences = FieldForcePreferences(application)

    init{
        getBillDashboard()
    }

    fun showAddBills() {
        _eventGoToAddBills.value = true
    }

    fun addBillsShown() {
        _eventGoToAddBills.value = false
    }

    private fun getBillDashboard() {
        coroutineScope.launch {
            val getBillDashboardDeferred = FieldForceApi.retrofitService.getBillListAsync(
                Constants.KEY,
                Constants.USER_ID,
                preferences.getLocation().latitude.toString(),
                preferences.getLocation().longitude.toString()
            )

            try {
                _progress.value = true
                val result = getBillDashboardDeferred.await()
                _billsDashboard.value = result.responseData
                _progress.value = false
            } catch (e: Exception) {
                _progress.value = false
            }
        }
    }
}
package com.suffix.fieldforce.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.suffix.fieldforce.model.BillDashboardResponseData
import com.suffix.fieldforce.networking.FieldForceApi
import com.suffix.fieldforce.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class BillsViewModel(application: Application) : AndroidViewModel(application) {
    val billDashboard = MutableLiveData<BillDashboardResponseData>()
    /*val billDashboard: LiveData<BillDashboardResponseData>
        get() = _billDashboard*/

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean>
        get() = _progress

    private val _eventGoToAddBills = MutableLiveData<Boolean>()
    val eventShowAddBills: LiveData<Boolean>
        get() = _eventGoToAddBills

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

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
                "23.7746479",
                "90.4031033"
            )

            try {
                _progress.value = true
                val result = getBillDashboardDeferred.await()
                billDashboard.value = result.responseData
                val pending = billDashboard.value!!.billStatObj.dashboards[0].approvedPendingAmount
                _progress.value = false
            } catch (e: Exception) {
                _progress.value = false
            }
        }
    }
}
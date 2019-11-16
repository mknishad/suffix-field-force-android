package com.suffix.fieldforce.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.suffix.fieldforce.R
import com.suffix.fieldforce.model.BillDashboardResponseData
import com.suffix.fieldforce.networking.FieldForceApi
import com.suffix.fieldforce.preference.FieldForcePreferences
import com.suffix.fieldforce.util.Constants
import kotlinx.coroutines.launch
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error

class StandardBillViewModel(application: Application) : BaseViewModel(application), AnkoLogger {
    private val _billsDashboard = MutableLiveData<BillDashboardResponseData>()
    val billsDashboard: LiveData<BillDashboardResponseData>
        get() = _billsDashboard

    private val _eventGoToAddBills = MutableLiveData<Boolean>()
    val eventShowAddBills: LiveData<Boolean>
        get() = _eventGoToAddBills

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
                progress.value = true
                val result = getBillDashboardDeferred.await()
                if (result.responseCode.equals("1", true)) {
                    _billsDashboard.value = result.responseData
                } else {
                    message.value = result.responseText
                }
                progress.value = false
            } catch (e: Exception) {
                error(e.message, e)
                progress.value = false
                message.value = getApplication<Application>().resources.getString(R.string.something_went_wrong)
            }
        }
    }
}
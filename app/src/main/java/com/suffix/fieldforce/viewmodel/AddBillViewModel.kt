package com.suffix.fieldforce.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.suffix.fieldforce.R
import com.suffix.fieldforce.model.AddBillResponse
import com.suffix.fieldforce.model.BillData
import com.suffix.fieldforce.model.BillType
import com.suffix.fieldforce.networking.FieldForceApi
import com.suffix.fieldforce.preference.FieldForcePreferences
import com.suffix.fieldforce.util.Constants
import kotlinx.coroutines.launch
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error

class AddBillViewModel(application: Application) : BaseViewModel(application), AnkoLogger {
    private val _billTypes = MutableLiveData<List<BillType>>()
    val billTypes: LiveData<List<BillType>>
    get() = _billTypes

    private val _addBillResponse = MutableLiveData<AddBillResponse>()
    val addBillResponse: LiveData<AddBillResponse>
    get() = _addBillResponse

    private val _eventAddBill = MutableLiveData<Boolean>()
    val eventAddBill: LiveData<Boolean>
    get() = _eventAddBill

    private val preferences: FieldForcePreferences = FieldForcePreferences(application)

    init {
        getBillTypes()
    }

    private fun getBillTypes() {
        coroutineScope.launch {
            val getBillTypesDeferred = FieldForceApi.retrofitService.getBillTypeAsync(
                Constants.KEY,
                Constants.USER_ID,
                preferences.getLocation().latitude.toString(),
                preferences.getLocation().longitude.toString()
            )

            try {
                progress.value = true
                val result = getBillTypesDeferred.await()
                _billTypes.value = result.responseData
                progress.value = false
            } catch (e: Exception) {
                error(e.message, e)
                progress.value = false
                message.value = getApplication<Application>().resources.getString(R.string.something_went_wrong)
            }
        }
    }

    fun submitBill(key: String, userId: String, lat: String, lng: String, billData: BillData) {
        val billDataJson = Gson().toJson(billData)

        coroutineScope.launch {
            val addBillDeferred = FieldForceApi.retrofitService.addBillAsync(
                key,
                userId,
                lat,
                lng,
                billDataJson
            )

            try {
                progress.value = true
                val result = addBillDeferred.await()
                _addBillResponse.value = result
                progress.value = false
            } catch (e: Exception) {
                error(e.message, e)
                progress.value = false
                message.value = getApplication<Application>().resources.getString(R.string.something_went_wrong)
            }
        }
    }
}

package com.suffix.fieldforce.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.suffix.fieldforce.model.BillType
import com.suffix.fieldforce.networking.FieldForceApi
import com.suffix.fieldforce.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AddBillViewModel(application: Application) : AndroidViewModel(application) {
    private val _billTypes = MutableLiveData<List<BillType>>()
    val billTypes: LiveData<List<BillType>>
    get() = _billTypes

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean>
        get() = _progress

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        getBillTypes()
    }

    private fun getBillTypes() {
        coroutineScope.launch {
            val getBillTypesDeferred = FieldForceApi.retrofitService.getBillTypeAsync(
                Constants.KEY,
                "BLA0010",
                "23.7746479",
                "90.4031033"
            )

            try {
                _progress.value = true
                val result = getBillTypesDeferred.await()
                _billTypes.value = result.responseData
                _progress.value = false
            } catch (e: Exception) {
                _progress.value = false
            }
        }
    }
}
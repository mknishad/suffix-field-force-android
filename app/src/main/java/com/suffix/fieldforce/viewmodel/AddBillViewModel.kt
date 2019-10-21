package com.suffix.fieldforce.viewmodel

import android.app.Application
import android.util.Base64
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.suffix.fieldforce.model.AddBillResponse
import com.suffix.fieldforce.model.BillData
import com.suffix.fieldforce.model.BillType
import com.suffix.fieldforce.networking.FieldForceApi
import com.suffix.fieldforce.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.nio.charset.StandardCharsets

class AddBillViewModel(application: Application) : AndroidViewModel(application) {
    private val _billTypes = MutableLiveData<List<BillType>>()
    val billTypes: LiveData<List<BillType>>
    get() = _billTypes

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean>
        get() = _progress

    private val _addBillResponse = MutableLiveData<AddBillResponse>()
    val addBillResponse: LiveData<AddBillResponse>
    get() = _addBillResponse

    private val _eventAddBill = MutableLiveData<Boolean>()
    val eventAddBill: LiveData<Boolean>
    get() = _eventAddBill

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

    fun submitBill(key: String, userId: String, lat: String, lng: String, billData: BillData) {
        val billDataString = Gson().toJson(billData)
        val billDataByteArray = billDataString.toByteArray()
        val billDataBase64 = Base64.encodeToString(billDataByteArray, Base64.DEFAULT)
        val decodedByte = Base64.decode(billDataBase64, Base64.DEFAULT)
        val decodedString = String(decodedByte, StandardCharsets.UTF_8)

        coroutineScope.launch {
            val addBillDeferred = FieldForceApi.retrofitService.addBillAsync(
                key,
                userId,
                lat,
                lng,
                billDataBase64
            //"eyJiaWxsRGF0ZSI6ICIyMDE5LTEwLTEwIiwgImJpbGxEYXRhT2JqIjogW3siYmlsbEFtb3VudCI6IDEwLjIwLCJiaWxsVHlwZUlkIjogMTAwMX0seyJiaWxsQW1vdW50IjogMTAwMCwiYmlsbFR5cGVJZCI6IDEwMDJ9LHsiYmlsbEFtb3VudCI6IDIwMC44OSwiYmlsbFR5cGVJZCI6IDEwMDN9LHsiYmlsbEFtb3VudCI6IDUwMDAsImJpbGxUeXBlSWQiOiAxMDA0fSx7ImJpbGxBbW91bnQiOiA2MDAsImJpbGxUeXBlSWQiOiAxMDA1fSx7ImJpbGxBbW91bnQiOiAxMDAuNjcsImJpbGxUeXBlSWQiOiAxMDA2fSx7ImJpbGxBbW91bnQiOiAyMCwiYmlsbFR5cGVJZCI6IDEwMDd9XSwicmVtYXJrcyI6ICJEYWlseSBCaWxsIGZvciB0aGUgbW9udGggb2YgT2N0IDIwMTkifQ=="
            )

            try {
                _progress.value = true
                val result = addBillDeferred.await()
                _addBillResponse.value = result
                _progress.value = false
            } catch (e: Exception) {
                _progress.value = false
            }
        }
    }
}

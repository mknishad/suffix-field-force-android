package com.suffix.fieldforce.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.suffix.fieldforce.R
import com.suffix.fieldforce.model.InventoryItem
import com.suffix.fieldforce.networking.FieldForceApi
import com.suffix.fieldforce.util.Constants
import kotlinx.coroutines.launch
import org.jetbrains.anko.error

class InventoryListViewModel(application: Application) : BaseViewModel(application) {
  private val _inventoryList = MutableLiveData<List<InventoryItem>>()
  val inventoryList: LiveData<List<InventoryItem>>
    get() = _inventoryList

  private val _eventOpenCreateRequisition = MutableLiveData<Boolean>()
  val eventOpenCreateRequisition: LiveData<Boolean>
    get() = _eventOpenCreateRequisition

  init {
    getInventoryList()
  }

  private fun getInventoryList() {
    progress.value = true
    coroutineScope.launch {
      try {
        val getInventoryListDeferred = FieldForceApi.retrofitService.getInventoryItemListAsync(
          Constants.KEY,
          preferences.getUser().userId,
          preferences.getLocation().latitude.toString(),
          preferences.getLocation().longitude.toString()
        )
        val result = getInventoryListDeferred.await()
        if (result.responseCode == "1") {
          _inventoryList.value = result.responseData
        } else {
          message.value = result.responseText
        }
      } catch (e: Exception) {
        error(e.message, e)
        message.value = getApplication<Application>().resources
          .getString(R.string.something_went_wrong)
      } finally {
        progress.value = false
      }
    }
  }

  fun openCreateRequisition() {
    _eventOpenCreateRequisition.value = true
  }
}

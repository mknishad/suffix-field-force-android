package com.suffix.fieldforce.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemRequisitionData(
  var requisitionDate: String,
  var itemRequisitionObj:  List<ItemRequisitionDataObj>?,
  var remarks: String
) : Parcelable

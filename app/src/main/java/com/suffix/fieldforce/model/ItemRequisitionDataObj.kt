package com.suffix.fieldforce.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemRequisitionDataObj(
  var quantity: Int?,
  var productInventoryId: Int?
) : Parcelable

package com.suffix.fieldforce.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Requisition(
  var id: String?,
  var inventories: List<Inventory>,
  var status: String
) : Parcelable

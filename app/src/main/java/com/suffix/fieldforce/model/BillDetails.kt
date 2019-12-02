package com.suffix.fieldforce.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BillDetails(
  var billName: String,
  var billAmount: String,
  var billTypeId: String
) : Parcelable

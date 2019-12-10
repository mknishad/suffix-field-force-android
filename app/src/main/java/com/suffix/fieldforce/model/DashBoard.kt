package com.suffix.fieldforce.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DashBoard(
  var approvedPendingAmount: Double,
  var disburseAmount: Double
) : Parcelable

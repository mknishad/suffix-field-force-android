package com.suffix.fieldforce.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BillDashboardResponseData(
  var billStatObj: BillStatObj,
  var billListObj: BillListObj
) : Parcelable
